package com.baro.ui.main

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
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
import com.baro.constants.PermissionsEnum
import com.baro.helpers.AsyncHelpers
import com.baro.helpers.PermissionsHelper
import com.baro.models.User
import com.baro.ui.learn.LearnActivity
import com.baro.ui.share.ShareActivity
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.ref.WeakReference
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var welcomeTextView: TextView
    private lateinit var accountButton: ImageButton
    private lateinit var shareButton: ImageButton
    private lateinit var learnButton: ImageButton

    private var user: User? = null

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Gets User Credentials
        user = intent.getParcelableExtra(AppTags.USER_OBJECT.name)

        // Configure UI
        configureAccountButton()
        configureShareButton()
        configureLearnButton()
        configureWelcomeTextView()

        // Update UI with User Credentials
        updateUserCredentials()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun updateUserCredentials() {
        runBlocking {
            launch {
                val weakReference = WeakReference<ContentResolver>(contentResolver)
                val bitmap = AsyncHelpers().loadUserThumbnail(user?.getThumbnailFile(), weakReference)
                onUserDataReturned(bitmap) }
        }
    }

    private fun configureWelcomeTextView() {
        welcomeTextView = findViewById(R.id.text_account)
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
            if (user != null) {
                val intentAccountActivity = Intent(
                        this@MainActivity,
                        AccountActivity::class.java)

                intentAccountActivity.putExtra(AppTags.USER_OBJECT.name, user)
                startActivity(intentAccountActivity)
            } else {
                // TODO Prompt activity to create an account (at least with picture and username)
                Toast.makeText(applicationContext, "DEBUG: Must create User", Toast.LENGTH_LONG).show()
            }
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

    private fun onUserDataReturned(imageBitmap: Bitmap?) {
        if (user != null) {
            welcomeTextView.text = "Welcome "+ user?.getUsername()
        } else {
            Toast.makeText(this@MainActivity, R.string.error_retrieving_credentials, Toast.LENGTH_LONG).show()
        }
        if (imageBitmap != null) {
            accountButton.setImageBitmap(imageBitmap)
        }
    }

}