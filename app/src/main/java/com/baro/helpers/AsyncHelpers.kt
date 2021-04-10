package com.baro.helpers

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import androidx.annotation.RequiresApi
import com.baro.constants.FileEnum
import com.baro.constants.JSONEnum
import com.baro.helpers.interfaces.*
import com.baro.models.Course
import com.baro.models.User
import java.io.File
import java.lang.ref.WeakReference
import java.nio.file.Paths
import java.util.*
import kotlin.collections.HashMap


class AsyncHelpers {
    /**
     * With this class you can call the following methods:
     * VerifyUserCredentials(callback)
     */

    ///////////////////////////////////////////////////////////////////////////////////////////////

    class VerifyUserCredentials(private var callback: OnUserLoginCheckComplete) : AsyncTask<File?, Void?, User?>() {
        @RequiresApi(api = Build.VERSION_CODES.O)


        override fun doInBackground(vararg externalFilesDir: File?): User? {

            val userMetaDataPath = Paths.get(externalFilesDir[0].toString(),
                    FileEnum.USER_DIRECTORY.key,
                    FileEnum.META_DATA_FILE.key)


            val userThumbnailPath = Paths.get(externalFilesDir[0].toString(),
                    FileEnum.USER_DIRECTORY.key,
                    FileEnum.PHOTO_THUMBNAIL_FILE.key)

            val userMetaDataFile = FileHelper.getFileAtPath(userMetaDataPath)
            val userThumbnailFile = FileHelper.getFileAtPath(userThumbnailPath)

            var user: User? = null

            if (userMetaDataFile != null) {
                val contentMetaData = FileHelper.readFile(userMetaDataFile)
                val jsonMetaData = contentMetaData?.let { JSONHelper.createJSONFromString(it) }

                user = if (userThumbnailFile != null && userThumbnailFile.length() > 0) {
                    User(
                            UUID.fromString(jsonMetaData?.get(JSONEnum.USER_UUID_KEY.key) as String),
                            jsonMetaData[JSONEnum.USER_NAME_KEY.key] as String,
                            userThumbnailFile
                    )
                } else {
                    User(
                            UUID.fromString(jsonMetaData?.get(JSONEnum.USER_UUID_KEY.key) as String),
                            jsonMetaData[JSONEnum.USER_NAME_KEY.key] as String)
                }
            }
            return user
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onPostExecute(result: User?) {
            callback.onUserLoginCheckDone(result)
        }

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    class UserCredentialsSave(private var callback: OnUserCredentialsSaveComplete) : AsyncTask<Context, Void?, Boolean?>() {
        @RequiresApi(api = Build.VERSION_CODES.P)
        override fun doInBackground(vararg context: Context): Boolean {
            // Save the Meta information
            saveCredentials(callback.getUsername(), callback.getPath())
            // Save Photo URI
            savePhotoUri(callback.getPhotoUri(), context[0])
            return true
        }


        override fun onPostExecute(result: Boolean?) {
            callback.onUserCredentialsSaveDone(result)
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private fun saveCredentials(username: String, path: String) {
            val credentialDetails = HashMap<String?, String?>()
            val userUUID = UUID.randomUUID()
            credentialDetails[JSONEnum.USER_NAME_KEY.key] = username
            credentialDetails[JSONEnum.USER_UUID_KEY.key] = userUUID.toString()
            val jsonCredentials = JSONHelper.createJSONFromHashMap(credentialDetails)
            val userMetaDataPath = Paths.get(path,
                    FileEnum.USER_DIRECTORY.key,
                    FileEnum.META_DATA_FILE.key)
            val userMetaDataFile = FileHelper.createFileAtPath(userMetaDataPath)
            FileHelper.writeToFile(userMetaDataFile, jsonCredentials.toString())
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        private fun savePhotoUri(photoUri: Uri?, context: Context) {
            if (photoUri != null) {

                val bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, photoUri))
                val userThumbnailPicturePath = Paths.get(context.getExternalFilesDir(null).toString(),
                        FileEnum.USER_DIRECTORY.key,
                        FileEnum.PHOTO_THUMBNAIL_FILE.key)
                val file = File(userThumbnailPicturePath.toString())

                FileHelper.writeBitmapToFile(file, bitmap)
            }
        }


    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    class LoadUserData(private var callback: OnUserDataFound) : AsyncTask<LoadUserData.TaskParams, Void?, LoadUserData.LoadUserDataResponse?>() {
        @RequiresApi(Build.VERSION_CODES.P)
        override fun doInBackground(vararg params: TaskParams?): LoadUserDataResponse? {
            val user = params[0]?.user
            val contentResolver = params[0]?.contentResolver
            if (user?.getThumbnailFile() != null) {
                val source = ImageDecoder.createSource(contentResolver!!, Uri.fromFile(user.getThumbnailFile()))
                val username = user.getUsername()
                val imageBmp = ImageDecoder.decodeBitmap(source)
                return LoadUserDataResponse(username, imageBmp)
            } else if (user?.getUsername() != null) {
                return LoadUserDataResponse(user.getUsername(), null)
            } else {
                return null
            }
        }

        @RequiresApi(Build.VERSION_CODES.P)
        override fun onPostExecute(result: LoadUserDataResponse?) {
            callback.onDataReturned(result)
        }

        class TaskParams(var user: User?, var contentResolver: ContentResolver)
        class LoadUserDataResponse(var username: String?, var imageBmp: Bitmap?)
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////

    class CourseCredentialsSave(private var callback: OnCourseCredentialsSaveComplete, private var context: WeakReference<Context>) : AsyncTask<CourseCredentialsSave.TaskParams, Void?, Boolean?>() {
        @RequiresApi(Build.VERSION_CODES.P)
        override fun doInBackground(vararg params: TaskParams?): Boolean? {
            // Save the Meta information
            val course = params[0]?.course
            val imageUri = params[0]?.imageUri

            if (course != null) {
                saveCredentials(course)
            }

            // Save Photo URI
            savePhotoUri(imageUri, course)

            return true
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private fun saveCredentials(course: Course) {

            val course = course

            val courseMetadata = HashMap<String?, String?>()

            courseMetadata[JSONEnum.USER_NAME_KEY.key] = course?.getCreator()?.getUserUUID().toString()
            courseMetadata[JSONEnum.COURSE_NAME_KEY.key] = course?.getCourseName()
            courseMetadata[JSONEnum.COURSE_UUID_KEY.key] = course?.getCourseUUID().toString()
            courseMetadata[JSONEnum.COURSE_LANGUAGE.key] = course?.getCourseCountry()?.getIsoCode()
            courseMetadata[JSONEnum.COURSE_CATEGORY.key] = course?.getCourseCategory()?.name

            val courseMetaDataPath = Paths.get(
                    context.get()?.getExternalFilesDir(null).toString(),
                    FileEnum.USER_DIRECTORY.key,
                    FileEnum.COURSE_DIRECTORY.key,
                    course?.getCourseUUID().toString(),
                    FileEnum.META_DATA_FILE.key
            )

            val courseMetaDataFile = FileHelper.createFileAtPath(courseMetaDataPath)


            FileHelper.writeToFile(courseMetaDataFile, courseMetadata.toString())
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        private fun savePhotoUri(photoUri: Uri?, course: Course?) {
            if (photoUri != null) {

                val bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.get()?.contentResolver!!, photoUri))
                val userThumbnailPicturePath = Paths.get(context.get()?.getExternalFilesDir(null).toString(),
                        FileEnum.USER_DIRECTORY.key,
                        FileEnum.COURSE_DIRECTORY.key,
                        course?.getCourseUUID().toString(),
                        FileEnum.PHOTO_THUMBNAIL_FILE.key)
                val file = File(userThumbnailPicturePath.toString())

                FileHelper.writeBitmapToFile(file, bitmap)
            }
        }


        @RequiresApi(Build.VERSION_CODES.P)
        override fun onPostExecute(result: Boolean?) {
            callback.onDataReturned(result)
        }

        class TaskParams(var course: Course?, var imageUri: Uri?)

    }


    class VideoUriSave(private var callback: OnVideoUriSaved, private var weakReferenceContentResolver: WeakReference<ContentResolver>) : AsyncTask<VideoUriSave.TaskParams, Void?, File?>() {
        override fun doInBackground(vararg params: TaskParams?): File? {
            val outputFile = params[0]?.outputFile
            val videoUri = params[0]?.videoUri

            if (outputFile != null && outputFile.exists()) {
                if (videoUri != null) {
                    return FileHelper.copyVideoToFile(outputFile, videoUri, weakReferenceContentResolver.get())
                }
            }
            return null

        }

        @RequiresApi(Build.VERSION_CODES.P)
        override fun onPostExecute(result: File?) {
            callback.onVideoUriSaved(result)
        }

        class TaskParams(var outputFile: File?, var videoUri: Uri?)

    }


    class DeleteFile(private var callback: OnDeleteFile) : AsyncTask<DeleteFile.TaskParams, Boolean?, Boolean?>() {
        override fun doInBackground(vararg params: TaskParams?): Boolean? {
            val deleteFile = params[0]?.deleteFile
            val deleteSlide = params[0]?.deleteSlide
            FileHelper.deleteFile(deleteFile)
            return deleteSlide
        }

        @RequiresApi(Build.VERSION_CODES.P)
        override fun onPostExecute(result: Boolean?) {
            callback.onDeleteFile(result)
        }

        class TaskParams(var deleteFile: File?, var deleteSlide: Boolean?)

    }


    class UpdateJSONFile(private var callback: OnUpdatedJSONFile) : AsyncTask<UpdateJSONFile.TaskParams, Boolean?, Boolean?>() {
        override fun doInBackground(vararg params: UpdateJSONFile.TaskParams?): Boolean? {
            val fileToUpdate = params[0]?.fileToUpdate
            val hashMapData = params[0]?.hashMapData

            var contents = FileHelper.readFile(fileToUpdate)

            var jsonContents = JSONHelper.createJSONFromString(contents!!)

            for ((key, value) in hashMapData!!) {
                jsonContents!!.put(key, value)
            }

            return FileHelper.writeToFile(fileToUpdate, jsonContents.toString())
        }

        @RequiresApi(Build.VERSION_CODES.P)
        override fun onPostExecute(result: Boolean?) {
            callback.onUpdatedJSONFile(result)
        }

        class TaskParams(var fileToUpdate: File?, var hashMapData: HashMap<String, ArrayList<String>>?)

    }
}
