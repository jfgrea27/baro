package com.baro.helpers

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.baro.R
import com.baro.constants.FileEnum
import com.baro.constants.JSONEnum
import com.baro.helpers.interfaces.OnUserCredentialsSaveComplete
import com.baro.helpers.interfaces.OnUserDataFound
import com.baro.helpers.interfaces.OnUserLoginCheckComplete
import com.baro.models.User
import com.baro.ui.main.MainActivity
import java.io.File
import java.nio.file.Paths
import java.util.*


class AsyncHelpers {
    /**
     * With this class you can call the following methods:
     * VerifyUserCredentials(callback)
     */


    class VerifyUserCredentials(private var callback: OnUserLoginCheckComplete) : AsyncTask<File?, Void?, User?>() {
        @RequiresApi(api = Build.VERSION_CODES.O)


        override fun doInBackground(vararg externalFilesDir: File?): User? {

            val userMetaDataPath = Paths.get(externalFilesDir[0].toString(),
                    FileEnum.USER_DIRECTORY.key,
                    FileEnum.META_DATA_FILE.key)


            val userThumbnailPath= Paths.get(externalFilesDir[0].toString(),
                    FileEnum.USER_DIRECTORY.key,
                    FileEnum.PHOTO_THUMBNAIL_FILE.key)

            val userMetaDataFile = FileHelper.getFileAtPath(userMetaDataPath)
            val userThumbnailFile = FileHelper.getFileAtPath(userThumbnailPath)

            var user : User? = null

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
        private fun savePhotoUri(photoUri:Uri?, context:Context) {
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
            }
            return null
        }

            @RequiresApi(Build.VERSION_CODES.P)
            override fun onPostExecute(result: LoadUserDataResponse?) {
               callback.onDataReturned(result)
            }
        class TaskParams(var user: User?, var contentResolver: ContentResolver)
        class LoadUserDataResponse(var username: String?, var imageBmp: Bitmap?)
        }




    }
