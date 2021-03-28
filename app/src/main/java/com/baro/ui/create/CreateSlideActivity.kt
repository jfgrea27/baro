package com.baro.ui.create

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.VideoView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import com.baro.R
import com.baro.constants.AppCodes
import com.baro.constants.AppTags
import com.baro.constants.FileEnum
import com.baro.constants.IntentEnum
import com.baro.dialogs.ImageDialog
import com.baro.helpers.FileHelper
import com.baro.models.Course
import com.baro.models.Slide
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.file.Paths
import java.util.*

class CreateSlideActivity : AppCompatActivity(), ImageDialog.OnInputListener {


    // UI
    private lateinit var nextButton: ImageButton
    private lateinit var previousButton: ImageButton
    private lateinit var playButton: ImageButton
    private lateinit var addDeleteButton: ImageButton
    private lateinit var videoView: VideoView

    // Model
    private lateinit var course: Course
    private var slideCounter = 0
    private var videoUri: Uri? = null
    private var isPlaying: Boolean = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_slide)

        // Get Course
        course = intent.getParcelableExtra(IntentEnum.COURSE.key)

        // Create First Slide
        val slide = Slide(UUID.randomUUID(), course)
        course.getSlides()?.add(slide)
        videoUri = course.slides?.get(slideCounter)?.getVideoUri()

        // Configure UI
        configureNextButton()
        configurePreviousButton()
        configurePlayButton()
        configureDeleteButton()
        configureVideoView()


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun configureDeleteButton() {
        addDeleteButton = findViewById(R.id.btn_add_delete)
        addDeleteButton.setOnClickListener {

            if (videoUri == null) {
                // TODO PERMISSION_REFACTOR
                val imageDialog = ImageDialog(this)
                imageDialog.show(supportFragmentManager, AppTags.THUMBNAIL_SELECTION.toString())
            } else {

                if (videoUri != null) {
                    var file = File(videoUri?.path)
                    FileHelper.deleteFile(file)
                    videoUri = null
                }
                videoView.setVideoURI(null)
                playButton.visibility = View.INVISIBLE
                videoView.visibility = View.INVISIBLE
                videoView.visibility = View.VISIBLE

                addDeleteButton.setImageResource(R.drawable.ic_create)
            }
        }
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
        playButton = findViewById(R.id.btn_play)
        playButton.visibility = View.INVISIBLE

        playButton.setOnClickListener {
            if (videoUri != null) {
                if (!isPlaying) {
                    isPlaying = true
                    videoView.start();
                    playButton.visibility = View.INVISIBLE

                }
            }
        }

    }

    private fun configureVideoView() {
        videoView = findViewById(R.id.video_view_slide)

        videoView.setOnClickListener {
            if (videoUri != null) {
                if (isPlaying) {
                    isPlaying = false
                    videoView.pause()
                    playButton.visibility = View.VISIBLE
                }
            }
        }

        videoView.setOnCompletionListener {
            playButton.visibility = View.VISIBLE
            isPlaying = false

        }

    }

    private var getGalleryContent: ActivityResultLauncher<String?>? = registerForActivityResult(ActivityResultContracts.GetContent()
    ) { uri ->
        videoUri = uri
        videoView.setVideoURI(videoUri)
        videoView.seekTo(100)

        // Update UI
        playButton.visibility = View.VISIBLE
        addDeleteButton.setImageResource(R.drawable.ic_delete)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private var getCameraContent: ActivityResultLauncher<Uri?>? = registerForActivityResult(
            ActivityResultContracts.TakeVideo()
    ) {
        // TODO Fix bug: Go to recording video but go back before recording one -> UI displayed is wrong
        videoView.setVideoURI(videoUri)
        videoView.seekTo(100)

        // Update UI
        playButton.visibility = View.VISIBLE
        addDeleteButton.setImageResource(R.drawable.ic_delete)
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun sendInput(choice: Int) {
        if (choice == AppCodes.CAMERA_ROLL_SELECTION.code) {
            val slideVideoPath = Paths.get(
                    this@CreateSlideActivity.getExternalFilesDir(null).toString(),
                    FileEnum.USER_DIRECTORY.key,
                    FileEnum.COURSE_DIRECTORY.key,
                    course.getCourseUUID().toString(),
                    FileEnum.SLIDE_DIRECTORY.key,
                    course.slides?.get(slideCounter)?.slideUUID.toString() + FileEnum.VIDEO_EXTENSION.key
            )
            val slideVideoFile = FileHelper.createFileAtPath(slideVideoPath)
            videoUri = FileProvider.getUriForFile(applicationContext!!, applicationContext!!.packageName + ".fileprovider", slideVideoFile!!)
            getCameraContent?.launch(videoUri)
        } else if (choice == AppCodes.GALLERY_SELECTION.code) {
            getGalleryContent?.launch("video/*")
        }
    }
}
