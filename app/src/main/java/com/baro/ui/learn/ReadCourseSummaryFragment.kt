package com.baro.ui.learn

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.baro.R
import com.baro.constants.*
import com.baro.helpers.AsyncHelpers
import com.baro.helpers.FileHelper
import com.baro.helpers.AsyncHelpers.OnCourseDeleted
import com.baro.models.Course
import com.baro.ui.share.p2p.WifiDirectActivity
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.nio.file.Paths


class ReadCourseSummaryFragment : Fragment() {

    // UI
    private lateinit var courseTitleText: TextView
    private lateinit var categoryTextView: TextView
    private lateinit var thumbnailView: ImageView
    private lateinit var languageButton: ImageView
    private lateinit var categoryButton: ImageView
    private lateinit var learnButton: ImageView
    private lateinit var deleteButton: ImageButton
    private lateinit var countryText: TextView
    private lateinit var sendButton: ImageButton
    // Model
    private lateinit var course: Course



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            course = it.getParcelable(COURSE_PARAM)!!
        }
    }


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_read_course_summary, container, false)

        // Configure UI
        configureCountryTextView(view)
        configureCourseTitleText(view)
        configureThumbnailView(view)
        configureLanguageView(view)
        configureCategoryView(view)
        configureDeleteButton(view)
        configurTextCategories(view)
        configureLearnButton(view)
        configureSendButton(view)

        updateUI()
        return view
    }

    private fun configureLearnButton(view: View) {
        learnButton = view.findViewById(R.id.btn_create)

        learnButton.setOnClickListener{
            val intentToSlideActivity = Intent(activity, LearnSlideActivity::class.java)

            intentToSlideActivity.putExtra(IntentEnum.COURSE.key, course)

            activity?.supportFragmentManager?.beginTransaction()
                ?.remove(this@ReadCourseSummaryFragment)
                ?.commit()
            startActivity(intentToSlideActivity)
        }
    }

    private fun configureSendButton(view: View) {
        sendButton = view.findViewById(R.id.btn_send)

        sendButton.setOnClickListener{
            val startWifiActivity = Intent(
                activity,
                WifiDirectActivity::class.java)

            startWifiActivity.putExtra(AppTags.COURSE_OBJECT.name, course)
            startWifiActivity.putExtra(AppTags.WIFIP2P_INTENT.name, AppCodes.WIFIP2P_PEER_SEND.code)
            startActivity(startWifiActivity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun configureDeleteButton(view: View) {
        deleteButton = view.findViewById(R.id.btn_delete)

        deleteButton.setOnClickListener{
            runBlocking {
                launch {
                    val done = AsyncHelpers().deleteCourse(context?.getExternalFilesDir(null), course)
                    if (!done) {
                        Toast.makeText(context, "Course Failed to Delete", Toast.LENGTH_SHORT).show()
                    }
                    val onCourseDeleted = (activity as OnCourseDeleted)
                    onCourseDeleted.onCourseDeleted(course)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateUI() {
        updateCourseTitle()
        updateLanguageIcon()
    }


    private fun updateLanguageIcon() {


        if (course?.getCourseCountry()?.getIsoCode() == null) {
            languageButton.setImageResource(R.drawable.ic_flag)
        } else {
            languageButton.setImageResource(course.getCourseCountry()?.getImageResource(requireContext())!!)

        }
    }

    private fun updateCourseTitle() {
        courseTitleText.text = course.getCourseName()
    }

    private fun configurTextCategories(view: View) {
        categoryTextView = view.findViewById(R.id.txt_category)
        var categories = ""

        for (category in course.getCourseCategory()) {
            categories += "$category "
        }

        if (categories == "") {
            categoryTextView.text = resources.getString(R.string.no_category)
        } else {
            categoryTextView.text = categories
        }
    }

    private fun configureCountryTextView(view: View) {
        countryText = view.findViewById(R.id.text_country)
        countryText.text = course.getCourseName()
    }

    private fun configureCourseTitleText(view: View) {
        courseTitleText = view.findViewById(R.id.edit_course_name)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun configureThumbnailView(view: View) {
        thumbnailView = view.findViewById(R.id.btn_course_thumbnail)
        val courseThumbnailPath = Paths.get(activity?.getExternalFilesDir(null).toString(),
            FileEnum.LEARN_DIRECTORY.key,
            course.getCourseUUID().toString(),
            FileEnum.PHOTO_THUMBNAIL_FILE.key)

        val courseThumbnailFile = FileHelper.createFileAtPath(courseThumbnailPath)
        thumbnailView.setImageURI(Uri.fromFile(courseThumbnailFile))

    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun configureCategoryView(view: View) {
        categoryButton = view.findViewById(R.id.btn_category)
        var string = ""
        for (category in course.getCourseCategory()){
            string += category.emoji + " "
        }
        if (string != "") {
            categoryTextView.text = string
        }
    }


    private fun configureLanguageView(view: View) {
        languageButton = view.findViewById(R.id.btn_language)
        if (course?.getCourseCountry()?.getIsoCode() == null) {
            languageButton.setImageResource(R.drawable.ic_flag)
        } else {
            languageButton.setImageResource(course.getCourseCountry()?.getImageResource(requireContext())!!)

        }
    }

    companion object {
        private val COURSE_PARAM = AppTags.COURSE_OBJECT.name

        @JvmStatic
        fun newInstance(course: Course?) =
                ReadCourseSummaryFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(COURSE_PARAM, course)
                    }
                }
    }

}