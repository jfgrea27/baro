package com.baro.ui.account

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.baro.R
import com.baro.constants.AppTags
import com.baro.helpers.AsyncHelpers
import com.baro.helpers.interfaces.OnUserDataFound
import com.baro.models.User


class AccountActivity : AppCompatActivity(), OnUserDataFound {
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
        val loadUserDataParams = AsyncHelpers.LoadUserData.TaskParams(user, this.contentResolver)
        val userRetrieveThumbnail = AsyncHelpers.LoadUserData(this)
        userRetrieveThumbnail.execute(loadUserDataParams)
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

    override fun onDataReturned(userData: AsyncHelpers.LoadUserData.LoadUserDataResponse?) {
        if (user != null) {
            val imageBmp = userData?.imageBmp
            if (imageBmp != null) {
                userThumbnailImageView.setImageBitmap(imageBmp)
            }
        }
    }


}