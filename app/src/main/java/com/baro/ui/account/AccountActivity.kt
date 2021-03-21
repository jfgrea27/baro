package com.baro.ui.account

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.baro.R
import com.baro.constants.AppTags
import com.baro.models.User


class AccountActivity : AppCompatActivity() {
    // UI
    private lateinit var userThumbnailImageView: ImageView
    private lateinit var followersButton: ImageButton
    private lateinit var settingsButton: ImageButton
    private lateinit var createButton: ImageButton
    private lateinit var courseRecycleView: RecyclerView

    // Model
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        // Gets User Credentials
        user = intent.getParcelableExtra(AppTags.USER_OBJECT.name)

        // Configure UI
        configureUserThumbnailImageview()
        configureFollowersButton()
        configureSettingsButton()
        configureCreateButton()
        configureRecycleView()

        // Update UI with User Credentials
        updateUserInfo()
    }

    private fun updateUserInfo() {
        var userRetrieveThumbnail = LoadUserData()
        userRetrieveThumbnail.execute()
    }


    private fun configureCreateButton() {
        createButton = findViewById(R.id.btn_create)

        createButton.setOnClickListener {
            // TODO __PERMISSION_REFACTOR__
        }
    }

    private fun configureUserThumbnailImageview() {
        userThumbnailImageView = findViewById(R.id.im_account)
    }

    private fun configureFollowersButton() {
        followersButton = findViewById(R.id.btn_followers)
        followersButton.setOnClickListener {
            // TODO - this will display the Users the current user is following - allows for deletion/access straight to their profile
        }
    }

    private fun configureSettingsButton() {
        settingsButton = findViewById(R.id.btn_settings)
        settingsButton.setOnClickListener {
            // TODO - this will display account settings: deleting account, changing password if Internet, etc.
        }
    }

    private fun configureRecycleView() {
        // TODO Gridview that holds the Courses
    }



    // TODO __ASYNC_REFACTOR__
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
                if (result != null) {
                    userThumbnailImageView.setImageBitmap(result)
                }
            }

        }
    }

}