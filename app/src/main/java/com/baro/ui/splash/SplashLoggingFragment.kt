package com.baroCCredentialsredentials.ui.splash

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.annotation.RequiresApi
import androidx.annotation.VisibleForTesting
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment

import com.baro.R
import com.baro.constants.AppCodes
import com.baro.constants.AppTags
import com.baro.constants.FileEnum
import com.baro.constants.JSONEnum
import com.baro.dialogs.ImageDialog
import com.baro.dialogs.ImageDialog.OnInputListener
import com.baro.helpers.FileHelper
import com.baro.helpers.JSONHelper
import com.baro.ui.main.MainActivity

import java.io.File
import java.nio.file.Paths
import java.util.*

@RequiresApi(api = Build.VERSION_CODES.N)
class SplashLoggingFragment : Fragment(), OnInputListener {

    private lateinit var photoThumbnailButton: ImageButton
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var nextButton: ImageButton
    private var cameraPermission = false
    private var readPermission = false
    private var writePermission = false
    private var photoUri: Uri? = null

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_logging, container, false)

        configurePhotoThumbnailButton(view)
        configureUsernameEditText(view)
        configurePasswordEditText(view)
        configureNextButton(view)
        return view
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun configurePhotoThumbnailButton(view: View?) {
        photoThumbnailButton = view?.findViewById(R.id.im_account )!!

        photoThumbnailButton.setOnClickListener { v: View? ->
            checkCameraPermissions()
            if (cameraPermission and readPermission and writePermission) {
                val imageDialog = ImageDialog(this)
                imageDialog.show(parentFragmentManager, AppTags.THUMBNAIL_SELECTION.toString())
            }
        }
    }

    private var getGalleryContent: ActivityResultLauncher<String?>? = registerForActivityResult(GetContent()
    ) { uri ->
        photoUri = uri
        photoThumbnailButton.setImageURI(uri)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private var getCameraContent: ActivityResultLauncher<Uri?>? = registerForActivityResult(
            TakePicture()
    ) { result: Boolean? ->
        if (result == true) {
            photoThumbnailButton.setImageURI(photoUri)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun sendInput(choice: Int) {
        if (choice == AppCodes.CAMERA_ROLL_SELECTION.code) {
            val userMetaDataPath = Paths.get(activity?.getExternalFilesDir(null).toString(),
                    FileEnum.USER_DIRECTORY.key,
                    FileEnum.PHOTO_THUMBNAIL_FILE.key)
            val userThumbnailFile = FileHelper.createFileAtPath(userMetaDataPath)
            photoUri = FileProvider.getUriForFile(activity?.applicationContext!!, activity?.applicationContext!!.packageName + ".fileprovider", userThumbnailFile!!)

        getCameraContent?.launch(photoUri)
        } else if (choice == AppCodes.GALLERY_SELECTION.code) {
            getGalleryContent?.launch("image/*")
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun configureNextButton(view: View?) {
        nextButton = view?.findViewById<ImageButton?>(R.id.btn_next)!!

        nextButton.setOnClickListener { v: View? ->
            if (usernameEditText.text.length > 5) {
                val userCredentialsSave = UserCredentialsSave()
                userCredentialsSave.execute()
            } else {
                // TODO discuss if we need a username..
                Toast.makeText(
                        context,
                        R.string.please_enter_valid_username_toast,
                        Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun configureUsernameEditText(view: View?) {
        usernameEditText = view?.findViewById(R.id.edit_text_username)!!
    }

    private fun configurePasswordEditText(view: View?) {
        passwordEditText = view?.findViewById(R.id.edit_text_password)!!
    }

    // Permissions
    private fun checkCameraPermissions() {
        val permissionsToBeGranted = ArrayList<String?>()
        if (ContextCompat.checkSelfPermission(
                        requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            permissionsToBeGranted.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            readPermission = true
        }
        if (ContextCompat.checkSelfPermission(
                        requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            permissionsToBeGranted.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            writePermission = true
        }
        if (ContextCompat.checkSelfPermission(
                        requireContext(), Manifest.permission.CAMERA) !=
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
    private val requestPermissionLauncher: ActivityResultLauncher<Array<String?>?>? = registerForActivityResult(RequestMultiplePermissions()) { permissions: MutableMap<String?, Boolean?>? ->
        if (permissions != null) {
            for ((key, value) in permissions) {
                value?.let { handlePermission(key, it) }
            }
        }
    }

    private fun handlePermission(permission: String?, isGranted: Boolean) {
        when (permission) {
            Manifest.permission.READ_EXTERNAL_STORAGE -> if (!isGranted) {
                Toast.makeText(context, R.string.read_storage_permission, Toast.LENGTH_LONG).show()
            } else {
                readPermission = true
            }
            Manifest.permission.WRITE_EXTERNAL_STORAGE -> if (!isGranted) {
                Toast.makeText(context, R.string.write_storage_permission, Toast.LENGTH_LONG).show()
            } else {
                writePermission = true
            }
            Manifest.permission.CAMERA -> if (!isGranted) {
                Toast.makeText(context, R.string.need_camera_access_toast, Toast.LENGTH_LONG).show()
            } else {
                cameraPermission = true
            }
            else -> {
            }
        }
    }


    private inner class UserCredentialsSave : AsyncTask<Void?, Void?, Boolean?>() {
        @RequiresApi(api = Build.VERSION_CODES.P)
        override fun doInBackground(vararg voids: Void?): Boolean? {
            // Save the Meta information
            saveCredentials()
            // Save Photo URI
            savePhotoUri()
            return true
        }

        override fun onPostExecute(result: Boolean?) {
            if (result == true) {
                val startMainActivity = Intent(
                        activity,
                        MainActivity::class.java)
                startActivity(startMainActivity)
                activity!!.finish()
            } else {
                Toast.makeText(context, R.string.error_saving_credentials, Toast.LENGTH_LONG).show()
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private fun saveCredentials() {
            val credentialDetails = HashMap<String?, String?>()
            val userUUID = UUID.randomUUID()
            credentialDetails[JSONEnum.USER_NAME_KEY.key] = usernameEditText.getText().toString()
            credentialDetails[JSONEnum.USER_UUID_KEY.key] = userUUID.toString()
            val jsonCredentials = JSONHelper.createJSONFromHashMap(credentialDetails)
            val userMetaDataPath = Paths.get(activity!!.getExternalFilesDir(null).toString(),
                    FileEnum.USER_DIRECTORY.key,
                    FileEnum.META_DATA_FILE.key)
            val userMetaDataFile = FileHelper.createFileAtPath(userMetaDataPath)
            FileHelper.writeToFile(userMetaDataFile, jsonCredentials.toString())
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        private fun savePhotoUri() {
            if (photoUri != null) {

                val bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().contentResolver, photoUri!!))
                val userThumbnailPicturePath = Paths.get(context!!.getExternalFilesDir(null).toString(),
                        FileEnum.USER_DIRECTORY.key,
                        FileEnum.PHOTO_THUMBNAIL_FILE.key)
                val file = File(userThumbnailPicturePath.toString())

                FileHelper.writeBitmapToFile(file, bitmap)
            }
        }
    }

}


