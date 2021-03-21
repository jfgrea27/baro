package com.baro.ui.create

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import com.baro.R
import com.baro.constants.AppTags
import com.baro.models.User


class CreateCourseSummaryFragment : Fragment()  {

    // UI
    private lateinit var courseTitleEditText: EditText
    private lateinit var thumbnailButton: ImageButton
    private lateinit var languageButton: ImageButton
    private lateinit var categoryButton: ImageButton
    private lateinit var createButton: ImageButton

    // Model
    private lateinit var user: User

    private var thumbnailUri: Uri? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getParcelable(USER_PARAM)!!
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

    }

    private fun configureCategoryButton(view: View) {
        categoryButton = view.findViewById(R.id.btn_category)
    }


    private fun configureLanguageButton(view: View) {
        languageButton = view.findViewById(R.id.btn_language)
    }

    private fun configureCreateButton(view: View) {
        createButton = view.findViewById(R.id.btn_create)
    }



//
//    private var getGalleryContent: ActivityResultLauncher<String?>? = registerForActivityResult(ActivityResultContracts.GetContent()
//    ) { uri ->
//        thumbnailUri = uri
//        thumbnailButton.setImageURI(uri)
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private var getCameraContent: ActivityResultLauncher<Uri?>? = registerForActivityResult(
//            ActivityResultContracts.TakePicture()
//    ) { result: Boolean? ->
//        if (result == true) {
//            thumbnailButton.setImageURI(thumbnailUri)
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    override fun sendInput(choice: Int) {
//        if (choice == AppCodes.CAMERA_ROLL_SELECTION.code) {
//            val userMetaDataPath = Paths.get(activity?.getExternalFilesDir(null).toString(),
//                    FileEnum.COURSE_DIRECTORY.key,
//                    FileEnum.PHOTO_THUMBNAIL_FILE.key)
//            val userThumbnailFile = FileHelper.createFileAtPath(userMetaDataPath)
//            thumbnailUri = FileProvider.getUriForFile(activity?.applicationContext!!, activity?.applicationContext!!.packageName + ".fileprovider", userThumbnailFile!!)
//
//            getCameraContent?.launch(thumbnailUri)
//        } else if (choice == AppCodes.GALLERY_SELECTION.code) {
//            getGalleryContent?.launch("image/*")
//        }
//    }


    companion object {
        private val USER_PARAM = AppTags.USER_OBJECT.name

        @JvmStatic
        fun newInstance(user: User?) =
                CreateCourseSummaryFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(USER_PARAM, user)
                    }
                }
    }

}