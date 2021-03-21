package com.baro.ui.main

import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.baro.AccountActivity
import com.baro.R
import com.baro.constants.FileEnum
import com.baro.constants.JSONEnum
import com.baro.helpers.FileHelper
import com.baro.helpers.JSONHelper
import com.baro.models.User
import com.baro.ui.create.CreateActivity
import com.baro.ui.learn.LearnActivity
import com.baro.ui.share.ShareActivity
import java.io.File
import java.nio.file.Paths
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var accountButton: ImageButton
    private lateinit var shareButton: ImageButton
    private lateinit var createButton: ImageButton
    private lateinit var learnButton: ImageButton

    private var user: User? = null
    private var thumbnailUri: Uri? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configureAccountButton()
        configureShareButton()
        configureCreateButton()
        configureLearnButton()

        updateUserInfo()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateUserInfo() {
        var userCredentialsRetrieve = UserCredentialsRetrieve()
        userCredentialsRetrieve.execute()
    }

    private fun configureLearnButton() {
        learnButton = findViewById(R.id.btn_learn)
        learnButton.setOnClickListener {
            val intentLearnActivity = Intent(
                    this@MainActivity,
                    LearnActivity::class.java)
            startActivity(intentLearnActivity)

        }
    }

    private fun configureAccountButton() {
        accountButton = findViewById(R.id.btn_account)
        accountButton.setOnClickListener {
            val intentAccountActivity = Intent(
                    this@MainActivity,
                    AccountActivity::class.java)
            startActivity(intentAccountActivity)
        }
    }

    private fun configureCreateButton() {
        createButton = findViewById(R.id.btn_create)
        createButton.setOnClickListener {
            val intentCreateCourse = Intent(
                    this@MainActivity,
                    CreateActivity::class.java)
            startActivity(intentCreateCourse)
        }
    }

    private fun configureShareButton() {
        shareButton = findViewById(R.id.btn_share)
        shareButton.setOnClickListener {
            val intentShareActivity = Intent(
                    this@MainActivity,
                    ShareActivity::class.java)
            startActivity(intentShareActivity)
        }
    }

    private inner class UserCredentialsRetrieve : AsyncTask<Void?, Void?, Boolean?>() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        override fun doInBackground(vararg voids: Void?): Boolean? {
            // Retrieve the Meta information
            user = retrieveUserCredentials()
            // Retrieve Thumbnail URI
            val uri = retrieveThumbnailUri()
            if (uri != null) {
                thumbnailUri = uri
            }
            return true
        }

        @RequiresApi(Build.VERSION_CODES.P)
        override fun onPostExecute(result: Boolean?) {
            if (result == true) {
                if (thumbnailUri != null) {
                    val source = ImageDecoder.createSource(contentResolver, thumbnailUri!!)
                    val bitmap = ImageDecoder.decodeBitmap(source)
                    accountButton.setImageBitmap(bitmap)
                }
            } else {
                Toast.makeText(this@MainActivity, R.string.error_retrieving_credentials, Toast.LENGTH_LONG).show()
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private fun retrieveUserCredentials(): User {
            val userMetaPath = Paths.get(
                    getExternalFilesDir(null).toString(),
                    FileEnum.USER_DIRECTORY.key,
                    FileEnum.META_DATA_FILE.key)
            val userMetaFile = File(userMetaPath.toString())
            val content = FileHelper.readFile(userMetaFile)
            val jsonObject = content?.let { JSONHelper.createJSONFromString(it) }

            return  User(
                    UUID.fromString(jsonObject?.get(JSONEnum.USER_UUID_KEY.key).toString()),
                    jsonObject?.get(JSONEnum.USER_NAME_KEY.key).toString()
            )
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private fun retrieveThumbnailUri(): Uri? {
            val userThumbnailPath = Paths.get(
                    getExternalFilesDir(null).toString(),
                    FileEnum.USER_DIRECTORY.key,
                    FileEnum.PHOTO_THUMBNAIL_FILE.key)
            val userThumbnailFile = File(userThumbnailPath.toString())
            return if (userThumbnailFile.exists()) {
                Uri.fromFile(userThumbnailFile)
            } else {
                null
            }

        }
    }

}