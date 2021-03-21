package com.baro.ui.main

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.baro.ui.account.AccountActivity
import com.baro.R
import com.baro.constants.AppTags
import com.baro.constants.FileEnum
import com.baro.constants.JSONEnum
import com.baro.helpers.FileHelper
import com.baro.helpers.JSONHelper
import com.baro.models.User
import com.baro.ui.learn.LearnActivity
import com.baro.ui.share.ShareActivity
import java.io.File
import java.nio.file.Paths
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var welcomeTextView: TextView
    private lateinit var accountButton: ImageButton
    private lateinit var shareButton: ImageButton
    private lateinit var learnButton: ImageButton

    private var user: User? = null
    private var thumbnailUri: Uri? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Gets user
        user = intent.getParcelableExtra(AppTags.USER_OBJECT.name)

        // Configure UI
        configureAccountButton()
        configureShareButton()
        configureLearnButton()
        configurWelcomeTextView()

        // Update UI with User Credentials
        updateUserInfo()
    }

    private fun configurWelcomeTextView() {
        welcomeTextView = findViewById(R.id.text_account)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateUserInfo() {
        var userRetrieveThumbnail = LoadUserData()
        userRetrieveThumbnail.execute()
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

    private fun configureShareButton() {
        shareButton = findViewById(R.id.btn_share)
        shareButton.setOnClickListener {
            val intentShareActivity = Intent(
                    this@MainActivity,
                    ShareActivity::class.java)
            startActivity(intentShareActivity)
        }
    }

    private inner class LoadUserData : AsyncTask<Void?, Void?, Bitmap?>() {
        @RequiresApi(api = Build.VERSION_CODES.P)
        override fun doInBackground(vararg voids: Void?): Bitmap? {
            if (user?.getThumbnailFile() != null) {
                val source = ImageDecoder.createSource(contentResolver, Uri.fromFile(user?.getThumbnailFile()))
                return ImageDecoder.decodeBitmap(source)
            }
            return null
        }

        @RequiresApi(Build.VERSION_CODES.P)
        override fun onPostExecute(result: Bitmap?) {
            if (user != null) {
                welcomeTextView.text = "Welcome "+ user!!.getUsername()
                if (result != null) {
                    accountButton.setImageBitmap(result)
                }
            } else {
                Toast.makeText(this@MainActivity, R.string.error_retrieving_credentials, Toast.LENGTH_LONG).show()
            }
        }
    }

}