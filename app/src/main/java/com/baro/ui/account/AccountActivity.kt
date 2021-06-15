package com.baro.ui.account

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baro.R
import com.baro.adapters.CourseAdapter
import com.baro.constants.AppTags
import com.baro.constants.FileEnum
import com.baro.constants.PermissionsEnum
import com.baro.helpers.AsyncHelpers
import com.baro.helpers.PermissionsHelper
import com.baro.helpers.interfaces.OnCourseCreate
import com.baro.helpers.interfaces.OnCourseCredentialsSaveComplete
import com.baro.helpers.AsyncHelpers.OnCourseDeleted
import com.baro.helpers.interfaceweaks.OnCreatorCourseCredentialsLoad
import com.baro.models.Course
import com.baro.models.User
import com.baro.ui.create.CreateCourseSummaryFragment
import com.baro.ui.create.EditCourseSummaryFragment
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.ref.WeakReference
import java.nio.file.Paths
import java.util.*
import kotlin.collections.ArrayList


class AccountActivity : AppCompatActivity(), OnCreatorCourseCredentialsLoad,
    CourseAdapter.OnCourseSelected, OnCourseCredentialsSaveComplete, OnCourseDeleted, OnCourseCreate{
    // UI
    private lateinit var userThumbnailImageView: ImageView
    private lateinit var followersButton: ImageButton
    private lateinit var settingsButton: ImageButton
    private lateinit var createButton: ImageButton
    private lateinit var courseRecycleView: RecyclerView

    // Model
    private var user: User? = null
    private lateinit var courses: ArrayList<Pair<Course, Uri?>>
    private var courseAdapter: CourseAdapter? = null

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        // Gets User Credentials
        user = intent.getParcelableExtra(AppTags.USER_OBJECT.name)

        // Configure UI
        configureUserThumbnailImageView()
        configureFollowersButton()
        configureSettingsButton()
        configureCreateButton()
        configureRecycleView()

        // Update UI with User Credentials
        updateUserCredentials()


    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun updateUserCredentials() {
        runBlocking {
            launch {
                val weakReference = WeakReference<ContentResolver>(contentResolver)
                val bitmap = AsyncHelpers().loadUserThumbnail(user?.getThumbnailFile(), weakReference)
                onUserDataReturned(bitmap) }
        }
    }

    override fun onBackPressed() {
        tellFragments()
        super.onBackPressed()
    }

    private fun tellFragments() {
        val fragments: List<Fragment> = supportFragmentManager.fragments
        for (f in fragments) {
            if (f is EditCourseSummaryFragment) {
                f.onBackPressed()
            }
        }
    }


    private fun configureCreateButton() {
        createButton = findViewById(R.id.btn_create)

        createButton.setOnClickListener {
            val weakReference = WeakReference<Activity>(this)
            if (PermissionsHelper.checkAndRequestPermissions(weakReference, PermissionsEnum.CREATE_COURSE_SELECTION)) {
                val course = Course(UUID.randomUUID(), user)
                course.setCreationDate(System.currentTimeMillis())
                val createCourseSummaryFragment: CreateCourseSummaryFragment =
                    CreateCourseSummaryFragment.newInstance(course)

                supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container_view, createCourseSummaryFragment, null)
                    .addToBackStack(AppTags.CREATE_COURSE_SUMMARY_FRAGMENT.name)
                    .setReorderingAllowed(true)
                    .commit()
            }



        }
    }

    private fun configureUserThumbnailImageView() {
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun configureRecycleView() {
        courseRecycleView = findViewById(R.id.grid_courses)
        courseRecycleView.layoutManager =
            GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)

        getCoursesFromFiles()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCoursesFromFiles() {
        val coursePath = Paths.get(
            getExternalFilesDir(null).toString(),
            FileEnum.USER_DIRECTORY.key,
            FileEnum.COURSE_DIRECTORY.key
        )
        val params = AsyncHelpers.CreatorCourseCredentialsLoad.TaskParams(coursePath, user)
        AsyncHelpers.CreatorCourseCredentialsLoad(this).execute(params)
    }

    private fun onUserDataReturned(imageBitmap: Bitmap?) {
        if (imageBitmap != null) {
            userThumbnailImageView.setImageBitmap(imageBitmap)
        }
    }


    override fun onCreatorCourseCredentialsLoad(courses: ArrayList<Pair<Course, Uri?>>) {
        this.courses = courses
        updateRecycleView()
    }

    private fun updateRecycleView() {
        courseAdapter?.notifyDataSetChanged()

        val weakReference = WeakReference<Context>(this)
        courseAdapter = CourseAdapter(weakReference, this.courses, this)
        courseRecycleView.adapter = courseAdapter
    }

    override fun notifyCourseSelected(course: Course) {

        val weakReference = WeakReference<Activity>(this)
        if (PermissionsHelper.checkAndRequestPermissions(weakReference, PermissionsEnum.READ_COURSE)) {
            val editCourseSummaryFragment: EditCourseSummaryFragment =
                EditCourseSummaryFragment.newInstance(course)

            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container_view, editCourseSummaryFragment, null)
                .addToBackStack(AppTags.EDIT_COURSE_SUMMARY_FRAGMENT.name)
                .setReorderingAllowed(true)
                .commit()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCourseDataReturned(result: Boolean?) {
        getCoursesFromFiles()
        courseAdapter?.notifyDataSetChanged()
    }

    override fun onCourseDeleted(result: Course?) {
        var position = -1

        for (course in courses) {
            if (result?.getCourseUUID() == course.first.getCourseUUID()) {
                position = courses.indexOf(course)
            }
        }

        if (position != -1) {
            courses.removeAt(position)
            courseAdapter?.notifyItemRemoved(position)
            courseAdapter?.notifyItemRangeChanged(position, courses.size)

        }

        supportFragmentManager.popBackStack()
    }

    override fun onCourseCreate(course: Pair<Course, Uri?>) {
        courses.add(course)
        courseAdapter?.notifyItemInserted(courses.size - 1)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        getCoursesFromFiles()
        courseAdapter?.notifyDataSetChanged()
    }
}