package com.baro.ui.create

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.baro.R
import com.baro.constants.*
import com.baro.dialogs.ImageDialog
import com.baro.helpers.AsyncHelpers
import com.baro.helpers.FileHelper
import com.baro.helpers.AsyncHelpers.OnCourseDeleted
import com.baro.models.Category
import com.baro.models.Country
import com.baro.models.Course
import com.baro.ui.account.AccountActivity
import com.baro.ui.dialogs.CategoryDialog
import com.baro.ui.dialogs.CountryDialog
import com.baro.ui.interfaces.OnBackPressed
import kotlinx.android.synthetic.main.dialog_image_chooser.view.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.ref.WeakReference
import java.nio.file.Paths
import java.util.*
import kotlin.collections.ArrayList


class EditCourseSummaryFragment : Fragment() , ImageDialog.OnInputListener, CountryDialog.CountrySelector, CategoryDialog.OnCategorySelected, OnBackPressed{

    // UI
    private lateinit var courseTitleEditText: EditText
    private lateinit var categoryTextView: TextView
    private lateinit var thumbnailButton: ImageButton
    private lateinit var languageButton: ImageButton
    private lateinit var categoryButton: ImageButton
    private lateinit var editButton: ImageButton
    private lateinit var deleteButton: ImageButton
    private lateinit var countryText: TextView
    // Model
    private lateinit var course: Course

    private var thumbnailUri: Uri? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            course = it.getParcelable(COURSE_PARAM)!!
        }
    }


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_edit_course_summary, container, false)

        // Configure UI
        configureCountryTextView(view)
        configureCourseTitleEditText(view)
        configureThumbnailButton(view)
        configureLanguageButton(view)
        configureCategoryButton(view)
        configureEditButton(view)
        configureDeleteButton(view)
        configureEditTextCategories(view)

        updateUI()
        return view
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
        updateCategoryTextView()
        updateLanguageIcon()
        updatePhotoThumbnail()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updatePhotoThumbnail() {
        val courseThumbnailPath = Paths.get(activity?.getExternalFilesDir(null).toString(),
                FileEnum.USER_DIRECTORY.key,
                FileEnum.COURSE_DIRECTORY.key,
                course.getCourseUUID().toString(),
                FileEnum.PHOTO_THUMBNAIL_FILE.key)
        val courseThumbnailFile = FileHelper.createFileAtPath(courseThumbnailPath)
        if (courseThumbnailFile?.length()!! > 0) {
            thumbnailButton.setImageURI(Uri.fromFile(courseThumbnailFile))
        }

    }

    private fun updateLanguageIcon() {


        if (course?.getCourseCountry()?.getIsoCode() == null) {
            languageButton.setImageResource(R.drawable.ic_flag)
        } else {
            languageButton.setImageResource(course.getCourseCountry()?.getImageResource(requireContext())!!)

        }
    }

    private fun updateCourseTitle() {
        courseTitleEditText.setText(course.getCourseName())
    }

    private fun configureEditTextCategories(view: View) {
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
    }

    private fun configureCourseTitleEditText(view: View) {
        courseTitleEditText = view.findViewById(R.id.edit_course_name)
    }

    private fun configureThumbnailButton(view: View) {
        thumbnailButton = view.findViewById(R.id.btn_course_thumbnail)


        thumbnailButton.setOnClickListener {
            val imageDialog = ImageDialog(this)
            imageDialog.show(parentFragmentManager, AppTags.THUMBNAIL_SELECTION.toString())
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun configureCategoryButton(view: View) {
        categoryButton = view.findViewById(R.id.btn_category)

        categoryButton.setOnClickListener{
            val categoryButton = CategoryDialog(this)
            categoryButton.show(parentFragmentManager, AppTags.CATEGORY_SELECTION.toString())
        }
    }

    override fun onCategorySelected(chosenCategories: HashSet<Category>) {
        var arrayListCategories = ArrayList<Category>(chosenCategories)
        course.setCourseCategory(arrayListCategories)


        updateCategoryTextView()
    }

    private fun updateCategoryTextView() {
        var string = ""
        for (category in course.getCourseCategory()){
            string += category.emoji + " "
        }
        if (string != "") {
            categoryTextView.text = string
        }

    }


    private fun configureLanguageButton(view: View) {
        languageButton = view.findViewById(R.id.btn_language)

        languageButton.setOnClickListener{
            val countryDialog = CountryDialog(this)
            countryDialog.show(parentFragmentManager, AppTags.THUMBNAIL_SELECTION.toString())
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCountrySelectedListener(country: Country?) {
        course.setCourseCountry(country)
        val weakContext = WeakReference<Context>(context)
        languageButton.setImageResource(country?.getImageResource(weakContext)!!)
        countryText.text = country.getCountryName(weakContext)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun configureEditButton(view: View) {
        editButton = view.findViewById(R.id.btn_create)
        editButton.setOnClickListener {
            val courseName = courseTitleEditText.text.toString()
            if (courseName.length > 3) {
                course.setCourseName(courseName)
                val weakContext = WeakReference<Context>(context)
                runBlocking {
                    launch{
                        val result = AsyncHelpers().courseCredentialsSave(course, thumbnailUri, weakContext)
                        onCourseDataReturned(result)
                    }
                }

            } else {
                Toast.makeText(context, getString(R.string.lenght_course_name_warning_toast), Toast.LENGTH_LONG).show()
            }
        }
    }


    private var getGalleryContent: ActivityResultLauncher<String?>? = registerForActivityResult(ActivityResultContracts.GetContent()
    ) { uri ->
        thumbnailUri = uri
        thumbnailButton.setImageURI(uri)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private var getCameraContent: ActivityResultLauncher<Uri?>? = registerForActivityResult(
            ActivityResultContracts.TakePicture()
    ) { result: Boolean? ->
        if (result == true) {
            thumbnailButton.setImageURI(thumbnailUri)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun sendInput(choice: Int) {
        if (choice == AppCodes.CAMERA_ROLL_SELECTION.code) {
            val courseThumbnailPath = Paths.get(activity?.getExternalFilesDir(null).toString(),
                    FileEnum.USER_DIRECTORY.key,
                    FileEnum.COURSE_DIRECTORY.key,
                    course.getCourseUUID().toString(),
                    FileEnum.PHOTO_THUMBNAIL_FILE.key)
            val courseThumbnailFile = FileHelper.createFileAtPath(courseThumbnailPath)

            thumbnailUri = FileProvider.getUriForFile(activity?.applicationContext!!, activity?.applicationContext!!.packageName + ".fileprovider", courseThumbnailFile!!)

            getCameraContent?.launch(thumbnailUri)
        } else if (choice == AppCodes.GALLERY_SELECTION.code) {
            getGalleryContent?.launch("image/*")
        }
    }


    companion object {
        private val COURSE_PARAM = AppTags.COURSE_OBJECT.name

        @JvmStatic
        fun newInstance(course: Course?) =
                EditCourseSummaryFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(COURSE_PARAM, course)
                    }
                }
    }

    fun onCourseDataReturned(result: Boolean?) {
        if (result == true) {
            val intentToSlideActivity = Intent(activity, CreateSlideActivity::class.java)

            intentToSlideActivity.putExtra(IntentEnum.COURSE.key, course)

            activity?.supportFragmentManager?.beginTransaction()
                ?.remove(this@EditCourseSummaryFragment)
                ?.commit()
            startActivity(intentToSlideActivity)
        }
    }


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onBackPressed() {
        val courseName = courseTitleEditText.text.toString()
        course.setCourseName(courseName)
        val weakContext = WeakReference<Context>(context)
//        val accountActivity = activity as OnCourseCredentialsSaveComplete
        runBlocking {
            launch {
                val result = AsyncHelpers().courseCredentialsSave(course, thumbnailUri, weakContext)
            }
        }
        val intentToAccountActivity = Intent(activity, AccountActivity::class.java)
        startActivity(intentToAccountActivity)
    }


}