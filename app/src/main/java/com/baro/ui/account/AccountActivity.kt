package com.baro.ui.account

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.baro.R
import com.baro.constants.AppCodes
import com.baro.constants.AppTags
import com.baro.constants.AppTags.USER_OBJECT
import com.baro.models.User
import com.baro.ui.create.CreateCourseSummaryFragment
import java.util.ArrayList

class AccountActivity : AppCompatActivity() {

    // UI
    private lateinit var userThumbnailImageView: ImageView
    private lateinit var followersButton: ImageButton
    private lateinit var settingsButton: ImageButton
    private lateinit var createButton: ImageButton
    private lateinit var courseRecycleView: RecyclerView

    // Model
    private var user: User? = null


    // TODO __PERMISSION_REFACTOR__
    private var cameraPermission = false
    private var readPermission = false
    private var writePermission = false




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        // Gets User Credentials
        user = intent.getParcelableExtra(USER_OBJECT.name)

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
            checkCameraPermissions()
            if (cameraPermission and readPermission and writePermission) {
                val createCourseSummaryFragment: CreateCourseSummaryFragment = CreateCourseSummaryFragment.newInstance(user)


                supportFragmentManager.beginTransaction()
                        .add(R.id.fragment_container_view, createCourseSummaryFragment, null)
                        .addToBackStack(AppTags.CREATE_COURSE_SUMMARY_FRAGMENT.name)
                        .setReorderingAllowed(true)
                        .commit()
            }
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
        // TODO GridView that holds the Courses
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

    // TODO __PERMISSION_REFACTOR__
    private fun checkCameraPermissions() {
        val permissionsToBeGranted = ArrayList<String?>()
        if (ContextCompat.checkSelfPermission(
                        applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            permissionsToBeGranted.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            readPermission = true
        }
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            permissionsToBeGranted.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            writePermission = true
        }
        if (ContextCompat.checkSelfPermission(
                        applicationContext, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            permissionsToBeGranted.add(Manifest.permission.CAMERA)
        } else {
            cameraPermission = true
        }
        if (permissionsToBeGranted.size > 0) {
            val permissions = permissionsToBeGranted.toTypedArray()
            requestPermissionLauncher!!.launch(permissions)
        }
    }

    @VisibleForTesting
    private val requestPermissionLauncher: ActivityResultLauncher<Array<String?>?>? = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions: MutableMap<String?, Boolean?>? ->
        if (permissions != null) {
            for ((key, value) in permissions) {
                value?.let { handlePermission(key, it) }
            }
        }
    }

    private fun handlePermission(permission: String?, isGranted: Boolean) {
        when (permission) {
            Manifest.permission.READ_EXTERNAL_STORAGE -> if (!isGranted) {
                Toast.makeText(applicationContext, R.string.read_storage_permission, Toast.LENGTH_LONG).show()
            } else {
                readPermission = true
            }
            Manifest.permission.WRITE_EXTERNAL_STORAGE -> if (!isGranted) {
                Toast.makeText(applicationContext, R.string.write_storage_permission, Toast.LENGTH_LONG).show()
            } else {
                writePermission = true
            }
            Manifest.permission.CAMERA -> if (!isGranted) {
                Toast.makeText(applicationContext, R.string.need_camera_access_toast, Toast.LENGTH_LONG).show()
            } else {
                cameraPermission = true
            }
        }
    }
}