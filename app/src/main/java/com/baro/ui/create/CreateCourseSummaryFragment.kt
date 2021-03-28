package com.baro.ui.create

import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.baro.R
import com.baro.constants.*
import com.baro.dialogs.ImageDialog
import com.baro.helpers.FileHelper
import com.baro.models.Course
import java.io.File
import java.nio.file.Paths
import java.time.LocalDate
import java.util.*


class CreateCourseSummaryFragment : Fragment() , ImageDialog.OnInputListener {

    // UI
    private lateinit var courseTitleEditText: EditText
    private lateinit var thumbnailButton: ImageButton
    private lateinit var languageButton: ImageButton
    private lateinit var categoryButton: ImageButton
    private lateinit var createButton: ImageButton

    // Model
    private lateinit var course: Course

    private var thumbnailUri: Uri? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            course = it.getParcelable(COURSE_PARAM)!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_create_course_summary, container, false)

        // Configure UI
        configureCourseTitleEditText(view)
        configureThumbnailButton(view)
        configureLanguageButton(view)
        configureCategoryButton(view)
        configureCreateButton(view)

        return view
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

    private fun configureCategoryButton(view: View) {
        categoryButton = view.findViewById(R.id.btn_category)
        // TODO Set the Course Category
    }


    private fun configureLanguageButton(view: View) {
        languageButton = view.findViewById(R.id.btn_language)
        // TODO Set the Course Language
    }

    private fun configureCreateButton(view: View) {
        createButton = view.findViewById(R.id.btn_create)
        createButton.setOnClickListener {
            val courseName = courseTitleEditText.text.toString()
            if (courseName.length > 3) {
                course.setCourseName(courseName)
                // TODO message if not selected a course category or language
                course.setCourseCategory(CategoryEnum.AGRICULTURE)
                course.setCourseLanguage(LanguageEnum.ENGLISH)

                val saveCourseData = SaveCourseData()
                saveCourseData.execute(course)

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
                CreateCourseSummaryFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(COURSE_PARAM, course)
                    }
                }
    }


    // TODO __ASYNC_REFACTOR__
    private inner class SaveCourseData : AsyncTask<Course?, Void?, Course?>() {
        @RequiresApi(api = Build.VERSION_CODES.P)
        override fun doInBackground(vararg courses: Course?): Course? {
            val course = courses[0]

            val courseMetadata = HashMap<String?, String?>()

            courseMetadata[JSONEnum.USER_NAME_KEY.key] = course?.getCreator()?.getUserUUID().toString()
            courseMetadata[JSONEnum.COURSE_NAME_KEY.key] = course?.getCourseName()
            courseMetadata[JSONEnum.COURSE_UUID_KEY.key] = course?.getCourseUUID().toString()
            courseMetadata[JSONEnum.COURSE_LANGUAGE.key] = course?.getCourseLanguage()?.name
            courseMetadata[JSONEnum.COURSE_CATEGORY.key] = course?.getCourseCategory()?.name

            val courseMetaDataPath = Paths.get(
                    activity?.getExternalFilesDir(null).toString(),
                    FileEnum.USER_DIRECTORY.key,
                    FileEnum.COURSE_DIRECTORY.key,
                    course?.getCourseUUID().toString(),
                    FileEnum.META_DATA_FILE.key
            )

            val courseMetaDataFile = FileHelper.createFileAtPath(courseMetaDataPath)


            FileHelper.writeToFile(courseMetaDataFile, courseMetadata.toString())



            if (thumbnailUri != null) {
                val courseThumbanilPath = Paths.get(
                        activity?.getExternalFilesDir(null).toString(),
                        FileEnum.USER_DIRECTORY.key,
                        FileEnum.COURSE_DIRECTORY.key,
                        course?.getCourseUUID().toString(),
                        FileEnum.PHOTO_THUMBNAIL_FILE.key
                )


                val bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().contentResolver, thumbnailUri!!))
                val file = File(courseThumbanilPath.toString())
                FileHelper.writeBitmapToFile(file, bitmap)
                course?.setThumbnailFile(file)
            }

            course?.setUpdateDate(LocalDate.now())
            return course
        }

        @RequiresApi(Build.VERSION_CODES.P)
        override fun onPostExecute(result: Course?) {
            if (result != null) {

                val intentToSlideActivity = Intent(activity, CreateSlideActivity::class.java)

                intentToSlideActivity.putExtra(IntentEnum.COURSE.key, result)

                activity?.supportFragmentManager?.beginTransaction()
                        ?.remove(this@CreateCourseSummaryFragment)
                        ?.commit()
                startActivity(intentToSlideActivity)
            }
        }
    }
}