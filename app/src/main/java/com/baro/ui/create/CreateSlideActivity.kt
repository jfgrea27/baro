package com.baro.ui.create

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.baro.R
import com.baro.constants.AppCodes
import com.baro.constants.AppTags
import com.baro.constants.FileEnum
import com.baro.constants.IntentEnum
import com.baro.dialogs.ImageDialog
import com.baro.helpers.FileHelper
import com.baro.models.Course
import com.baro.models.Slide
import java.io.File
import java.nio.file.Paths

class CreateSlideActivity : AppCompatActivity(), ImageDialog.OnInputListener {


    // UI
    private lateinit var nextButton: ImageButton
    private lateinit var previousButton: ImageButton
    private lateinit var playEditButton: ImageButton
    private lateinit var deleteButton: ImageButton
    private lateinit var videoView: VideoView

    // Model
    private lateinit var course: Course
    private var slideCounter = 0
    private var videoUri: Uri? = null
    private var isPlaying: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_slide)

        // Get Course
        course = intent.getParcelableExtra(IntentEnum.COURSE.key)

        // Configure UI
        configureNextButton()
        configurePreviousButton()
        configurePlayButton()
        configureDeleteButton()
        configureVideoView()


    }

    private fun configureDeleteButton() {
        deleteButton = findViewById(R.id.btn_delete)
        deleteButton.visibility = View.INVISIBLE
    }

    private fun configurePreviousButton() {
        previousButton = findViewById(R.id.btn_previous)

        // Initially invisible
        previousButton.visibility = View.INVISIBLE
    }

    private fun configureNextButton() {
        nextButton = findViewById(R.id.btn_next)

        nextButton.visibility = View.INVISIBLE

    }

    private fun configurePlayButton() {
        playEditButton = findViewById(R.id.btn_play_edit)

        playEditButton.setOnClickListener{
            // Assume that permissoins are already granted at this point - is this good practise?
            if (!isPlaying && videoUri == null) {
                val imageDialog = ImageDialog(this)
                imageDialog.show(supportFragmentManager, AppTags.THUMBNAIL_SELECTION.toString())

                // TODO plan of attack - save the file using Async and update UI to show the play button and the videoUri to not null
                // TODO when file saved - add a slide to the arraylist of the course with same UUID.
                // TODO update the previous and next arrows depending on whether you are the first/last slide

            } else {
                if (!isPlaying) {
                    isPlaying = true
                    playEditButton.visibility = View.INVISIBLE

                    // Plays the video, when finished, make playEditButton visible, set isPlaying to false
                }

            }

        }

    }

    private fun configureVideoView() {
        videoView = findViewById(R.id.video_view_slide)

    }

    private var getGalleryContent: ActivityResultLauncher<String?>? = registerForActivityResult(ActivityResultContracts.GetContent()
    ) { uri ->
        videoUri = uri



    }

    @RequiresApi(Build.VERSION_CODES.O)
    private var getCameraContent: ActivityResultLauncher<Uri?>? = registerForActivityResult(
            ActivityResultContracts.TakePicture()
    ) { result: Boolean? ->
        if (result == true) {
            // TODO complete this part -> need to save the image in a temp location in case the person does not want to use it..
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun sendInput(choice: Int) {
        if (choice == AppCodes.CAMERA_ROLL_SELECTION.code) {
            val userMetaDataPath = Paths.get(this@CreateSlideActivity.getExternalFilesDir(null).toString(),
                    FileEnum.COURSE_DIRECTORY.key,
                    FileEnum.PHOTO_THUMBNAIL_FILE.key)
            val userThumbnailFile = FileHelper.createFileAtPath(userMetaDataPath)
            videoUri = FileProvider.getUriForFile(applicationContext!!, applicationContext!!.packageName + ".fileprovider", userThumbnailFile!!)

            getCameraContent?.launch(videoUri)
        } else if (choice == AppCodes.GALLERY_SELECTION.code) {
            getGalleryContent?.launch("video/*")
        }
    }
    

}