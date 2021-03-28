package com.baro.ui.main

import android.content.Intent
import android.net.Uri
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
import com.baro.helpers.AsyncHelpers
import com.baro.helpers.interfaces.OnUserDataFound
import com.baro.models.User
import com.baro.ui.learn.LearnActivity
import com.baro.ui.share.ShareActivity
import java.util.*


class MainActivity : AppCompatActivity(), OnUserDataFound {
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

        // Gets User Credentials
        user = intent.getParcelableExtra(AppTags.USER_OBJECT.name)

        // Configure UI
        configureAccountButton()
        configureShareButton()
        configureLearnButton()
        configureWelcomeTextView()

        // Update UI with User Credentials
        val loadUserDataParams = AsyncHelpers.LoadUserData.TaskParams(user, this.contentResolver)
        val userRetrieveThumbnail = AsyncHelpers.LoadUserData(this)
        userRetrieveThumbnail.execute(loadUserDataParams)
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




    override fun onDataReturned(userData: AsyncHelpers.LoadUserData.LoadUserDataResponse?) {
        val username = userData?.username
        val imageBmp = userData?.imageBmp
        if (user != null) {
            welcomeTextView.text = "Welcome "+ username
            if (imageBmp != null) {
                accountButton.setImageBitmap(imageBmp)
            }
        } else {
            Toast.makeText(this@MainActivity, R.string.error_retrieving_credentials, Toast.LENGTH_LONG).show()
        }
    }

}