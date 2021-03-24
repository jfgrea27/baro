package com.baro.helpers

import android.os.AsyncTask
import android.os.Build
import androidx.annotation.RequiresApi
import com.baro.constants.FileEnum
import com.baro.constants.JSONEnum
import com.baro.models.User
import java.io.File
import java.nio.file.Paths
import java.util.*


class AsyncHelpers {
    /**
     * With this class you can call the following methods:
     */

    class VerifyUserCredentials(private var callback: OnUserCheckComplete) : AsyncTask<File?, Void?, User?>() {
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
            callback.onTaskDone(result)
        }

    }

}