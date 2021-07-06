package com.baro.ui.learn

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.VideoView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import com.baro.R
import com.baro.constants.AppCodes
import com.baro.constants.FileEnum
import com.baro.constants.IntentEnum
import com.baro.models.Course
import com.baro.models.Slide
import kotlinx.android.synthetic.main.dialog_image_chooser.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.nio.file.Paths
import java.util.*


class LearnSlideActivity : AppCompatActivity() {

    // UI
    private lateinit var nextButton: ImageButton
    private lateinit var previousButton: ImageButton
    private lateinit var playButton: ImageButton
    private lateinit var videoView: VideoView


    // Model
    private lateinit var course: Course
    private var slideCounter = 0
    private var videoUri: Uri? = null
    private var isPaused: Boolean = true

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn_slide)

        // Get Course
        course = intent.getParcelableExtra(IntentEnum.COURSE.key)!!

        populateSlideVideoUri()

        videoUri = course.getSlides()[slideCounter].getVideoUri()


        // Configure UI
        configureNextButton()
        configurePreviousButton()
        configurePlayButton()
        configureVideoView()
        updateUI()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun populateSlideVideoUri() {
        for (slide in course.getSlides()) {
            val slidePath = Paths.get(
                this@LearnSlideActivity.getExternalFilesDir(null).toString(),
                FileEnum.LEARN_DIRECTORY.key,
                course.getCourseUUID().toString(),
                FileEnum.SLIDE_DIRECTORY.key,
                slide.slideUUID.toString() + FileEnum.VIDEO_EXTENSION.key
            )
            slide.setVideoUri(Uri.fromFile(slidePath.toFile()))
        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun configurePreviousButton() {
        previousButton = findViewById(R.id.btn_previous)

        // Initially invisible
        previousButton.visibility = View.INVISIBLE

        previousButton.setOnClickListener {
            updateClickable(allUnclickable = true)
            setVideoURI(AppCodes.BACKWARDS_SLIDE)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun configureNextButton() {
        nextButton = findViewById(R.id.btn_next)

        nextButton.visibility = View.INVISIBLE

        nextButton.setOnClickListener {
            updateClickable(allUnclickable = true)
            setVideoURI(AppCodes.FORWARD_SLIDE)
        }
    }

    private fun updateUI() {
        updateClickable()
        updateVideoViewUI()
        updatePlayButtonUI()
        updateSlideCountUI()

    }


    private fun updateClickable(allUnclickable: Boolean = false) {

        if (allUnclickable) {
            nextButton.isClickable = false
            previousButton.isClickable = false
            playButton.isClickable = false
            videoView.isClickable = false
        } else {
            if (!isPaused) {
                nextButton.isClickable = false
                previousButton.isClickable = false
                playButton.isClickable = false
                videoView.isClickable = true

            } else {
                nextButton.isClickable = true
                previousButton.isClickable = true
                playButton.isClickable = true
                videoView.isClickable = false

            }
        }
    }

    private fun updateVideoViewUI() {
        if (videoUri == null) {
            videoView.setVideoURI(null)
            videoView.visibility = View.INVISIBLE
            videoView.visibility = View.VISIBLE
        } else if (isPaused) {
            videoView.setVideoURI(videoUri)

            if (videoUri != null) {
                videoView.seekTo(100)

            }
        }
    }

    private fun updatePlayButtonUI() {
        if (videoUri == null || !isPaused) {
            playButton.visibility = View.INVISIBLE
        } else {
            playButton.visibility = View.VISIBLE
        }

    }

    private fun updateSlideCountUI() {

        when (slideCounter) {
            0 -> {
                if (course.getSlides().size > 1 || videoUri != null) {
                    nextButton.visibility = View.VISIBLE
                } else if (videoUri == null) {
                    nextButton.visibility = View.INVISIBLE
                }
                previousButton.visibility = View.INVISIBLE

            }
            course.getSlides().size.minus(1) -> {
                if (videoUri != null) {
                    nextButton.visibility = View.VISIBLE
                } else {
                    nextButton.visibility = View.INVISIBLE
                }
                previousButton.visibility = View.VISIBLE
            }
            else -> {
                nextButton.visibility = View.VISIBLE
                previousButton.visibility = View.VISIBLE
            }
        }


    }



    private fun configurePlayButton() {
        playButton = findViewById(R.id.btn_play)
        playButton.visibility = View.INVISIBLE

        playButton.setOnClickListener {
            if (videoUri != null) {
                if (isPaused) {
                    isPaused = false
                    videoView.start()
                    updateUI()
                }
            }
        }

    }

    private fun configureVideoView() {
        videoView = findViewById(R.id.video_view_slide)

        videoView.setOnClickListener {
            if (videoUri != null) {
                if (!isPaused) {

                    isPaused = true
                    videoView.pause()
                    updateUI()

                }
            }
        }

        videoView.setOnCompletionListener {
            isPaused = true
            updateUI()
        }

    }


    private fun setVideoURI(direction: AppCodes) {
        runBlocking{
            launch{
                course.getSlides()[slideCounter].setVideoUri(videoUri)
                if (direction == AppCodes.FORWARD_SLIDE) {
                    slideCounter += 1
                    if (course.getSlides().size == slideCounter) {
                        val slide = Slide(UUID.randomUUID())
                        slide.setCourse(course)
                        course.getSlides().add(slide)
                    }

                    val file = course.getSlides()[slideCounter].getVideoUri()?.toFile()

                    videoUri = if (file?.exists() == true) {
                        course.getSlides()[slideCounter].getVideoUri()
                    } else {
                        null
                    }

                } else if (direction == AppCodes.BACKWARDS_SLIDE) {
                    if (videoUri == null && slideCounter == course.getSlides().size.minus(1) && slideCounter > 0) {
                        course.getSlides().removeAt(course.getSlides().size.minus(1))
                    }
                    slideCounter -= 1

                    val file = course.getSlides()[slideCounter].getVideoUri()?.toFile()

                    if (file?.exists() == true) {
                        videoUri = course.getSlides()[slideCounter].getVideoUri()
                    } else {
                        videoUri = null
                    }
                }
                updateUI()
            }
        }
    }


}
