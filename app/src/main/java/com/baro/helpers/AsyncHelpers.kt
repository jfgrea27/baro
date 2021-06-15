package com.baro.helpers

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import androidx.annotation.RequiresApi
import com.baro.constants.CategoryEnum
import com.baro.constants.FileEnum
import com.baro.constants.JSONEnum
import com.baro.helpers.interfaces.*
import com.baro.helpers.interfaceweaks.OnCreatorCourseCredentialsLoad
import com.baro.models.Country
import com.baro.models.Course
import com.baro.models.User
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.lang.ref.WeakReference
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class AsyncHelpers {
    /**
     * With this class you can call the following methods:
     * VerifyUserCredentials(callback)
     */

    ///////////////////////////////////////////////////////////////////////////////////////////////


    @RequiresApi(api = Build.VERSION_CODES.O)
    fun verifyUserCredentials(file: File?): User? {


        val userMetaDataPath = Paths.get(file.toString(),
            FileEnum.USER_DIRECTORY.key,
            FileEnum.META_DATA_FILE.key)


        val userThumbnailPath = Paths.get(file.toString(),
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



    ////////////////////////////////////////////////////////////////////////////////////////////////

    @RequiresApi(Build.VERSION_CODES.P)
    fun userCredentialsSave(username: String, path: String, photoUri: Uri?, weakReference: WeakReference<Context>): Boolean {
        savePhotoUri(photoUri, weakReference.get())
        return saveCredentials(username, path)
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun saveCredentials(username: String, path: String): Boolean {

        val credentialDetails = HashMap<String?, String?>()
        val userUUID = UUID.randomUUID()
        credentialDetails[JSONEnum.USER_NAME_KEY.key] = username
        credentialDetails[JSONEnum.USER_UUID_KEY.key] = userUUID.toString()
        val jsonCredentials = JSONHelper.createJSONFromHashMap(credentialDetails)
        val userMetaDataPath = Paths.get(
            path,
            FileEnum.USER_DIRECTORY.key,
            FileEnum.META_DATA_FILE.key
        )
        val userMetaDataFile = FileHelper.createFileAtPath(userMetaDataPath)
        return FileHelper.writeToFile(userMetaDataFile, jsonCredentials.toString())
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private fun savePhotoUri(photoUri: Uri?, context: Context?) {
        if (photoUri != null) {
            if (context != null) {
                val bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, photoUri))
                val userThumbnailPicturePath = Paths.get(
                    context.getExternalFilesDir(null).toString(),
                    FileEnum.USER_DIRECTORY.key,
                    FileEnum.PHOTO_THUMBNAIL_FILE.key)
                val file = File(userThumbnailPicturePath.toString())
                FileHelper.writeBitmapToFile(file, bitmap)}


        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////


    @RequiresApi(Build.VERSION_CODES.P)
    fun loadUserThumbnail(thumbnailFile: File?, weakReferenceContentResolver: WeakReference<ContentResolver>): Bitmap? {
        val content = weakReferenceContentResolver.get()
        return if (thumbnailFile != null && thumbnailFile.exists()) {
            val source = ImageDecoder.createSource(content!!, Uri.fromFile(thumbnailFile))
            ImageDecoder.decodeBitmap(source)
        } else {
            null
        }
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
            courseMetadata[JSONEnum.COURSE_CREATION_DATETIME.key] = course.getCreationDate()?.toString()
            val isoCode = course.getCourseCountry()?.getIsoCode()
            if (isoCode == null) {
                courseMetadata[JSONEnum.COURSE_LANGUAGE.key] = "null"
            } else {
                courseMetadata[JSONEnum.COURSE_LANGUAGE.key] = course.getCourseCountry()?.getIsoCode()
            }
            courseMetadata[JSONEnum.COURSE_CATEGORY.key] = JSONArray(course.getCourseCategory().toString()).toString()
            courseMetadata[JSONEnum.COURSE_SLIDES.key] = JSONArray(course.getSlides().toString()).toString()

            val courseMetaDataPath = Paths.get(
                    context.get()?.getExternalFilesDir(null).toString(),
                    FileEnum.USER_DIRECTORY.key,
                    FileEnum.COURSE_DIRECTORY.key,
                    course?.getCourseUUID().toString(),
                    FileEnum.META_DATA_FILE.key
            )

            val courseMetaDataFile = FileHelper.createFileAtPath(courseMetaDataPath)
            val courseJSONMeta = JSONHelper.createJSONFromHashMap(courseMetadata)


            FileHelper.writeToFile(courseMetaDataFile, courseJSONMeta.toString())
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
            callback.onCourseDataReturned(result)
        }

        class TaskParams(var course: Course?, var imageUri: Uri?)

    }


    class CreatorCourseCredentialsLoad(private var callback: OnCreatorCourseCredentialsLoad) : AsyncTask<CreatorCourseCredentialsLoad.TaskParams, Void?, ArrayList<Pair<Course, Uri?>>>() {
        @RequiresApi(Build.VERSION_CODES.P)
        override fun doInBackground(vararg params: TaskParams?): ArrayList<Pair<Course, Uri?>>? {
            var courses = ArrayList<Pair<Course, Uri?>>()

            val path = params[0]?.path
            val coursesFile = FileHelper.createDirAtPath(path)
            val user = params[0]?.user
            if (coursesFile.listFiles() != null) {
                for (courseFolder in coursesFile.listFiles()) {

                    if (courseFolder.isDirectory) {
                        val jsonFilePath = Paths.get(courseFolder.toString(), FileEnum.META_DATA_FILE.key)

                        val jsonFile = jsonFilePath.toFile()

                        // Retrieve content file
                        val contents = FileHelper.readFile(jsonFile)

                        if (contents != null) {
                            val jsonContents = JSONHelper.createJSONFromString(contents!!)

                            // Course UUID
                            val courseUUID = jsonContents?.get(JSONEnum.COURSE_UUID_KEY.key)
                            // Course Name
                            val courseName = jsonContents?.get(JSONEnum.COURSE_NAME_KEY.key)
                            // Category
                            val categoryJSON = JSONArray(jsonContents?.get(JSONEnum.COURSE_CATEGORY.key).toString())
                            // Language
                            val language = jsonContents?.get(JSONEnum.COURSE_LANGUAGE.key)
                            // Timestamp
                            val courseCreationTimestamp = jsonContents?.get(JSONEnum.COURSE_CREATION_DATETIME.key).toString().toLong()
                            // Slides
                            val slidesJSON = JSONArray(jsonContents?.get(JSONEnum.COURSE_SLIDES.key).toString())

                            // Creation course
                            val course = Course(UUID.fromString(courseUUID as String?), user)

                            //  Adding course name
                            course.setCourseName(courseName as String)
                            // Adding Course Category
                            course.setCourseCategory(CategoryEnum.getCategoriesFromJSONArray(categoryJSON as JSONArray))
                            // Adding Course language
                            if (language.toString() == "null") {
                                course.setCourseCountry(Country(null))
                            } else {
                                course.setCourseCountry(Country(language.toString()))

                            }
                            // Adding timestamp
                            course.setCreationDate(courseCreationTimestamp)

                            // Adding Slides
                            course.setSlidesFromJSONArray(slidesJSON)

                            // Thumbnail
                            val imagePath = Paths.get(courseFolder.toString(), FileEnum.PHOTO_THUMBNAIL_FILE.key)
                            val imageFile = imagePath.toFile()
                            val imageUri = Uri.fromFile(imageFile)

                            val pair = Pair<Course, Uri>(course, imageUri)
                            courses.add(pair)
                        }
                    }
                }
            }

            var arrayListResults = ArrayList<Pair<Course, Uri?>>()

            if (courses.size > 1) {
                courses.sortedBy { it.first }
            }
            for (course in courses) {
                arrayListResults.add(course)
            }
            return arrayListResults

        }

        @RequiresApi(Build.VERSION_CODES.P)
        override fun onPostExecute(result: ArrayList<Pair<Course, Uri?>>) {
            callback.onCreatorCourseCredentialsLoad(result)
        }

        class TaskParams(var path: Path?, var user: User?)
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////
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


    fun updateJSONFile(courseMetaFile: File, slideHashMap: java.util.HashMap<String, java.util.ArrayList<String>>): Boolean {
        //TODO - Account for null jsonContents
        val contents = FileHelper.readFile(courseMetaFile)

        val jsonContents = JSONHelper.createJSONFromString(contents!!)

        for ((key, value) in slideHashMap) {
            jsonContents!!.put(key, value)
        }
        return FileHelper.writeToFile(courseMetaFile, jsonContents.toString())
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteCourse(rootDir: File?, course: Course): Boolean {
        if (rootDir?.exists() == true) {
            val coursePath = Paths.get(rootDir.toString(),
            FileEnum.USER_DIRECTORY.key,
            FileEnum.COURSE_DIRECTORY.key,
            course.getCourseUUID().toString())
            val courseFile = coursePath.toFile()
            FileHelper.deleteFile(courseFile)
            return true} else {return false}
    }



    interface OnCourseDeleted {
        fun onCourseDeleted(result: Course?)
    }


}
