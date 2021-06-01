package com.baro.ui.account

import android.content.Context
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
import com.baro.helpers.AsyncHelpers
import com.baro.helpers.interfaces.OnCourseCreate
import com.baro.helpers.interfaces.OnCourseCredentialsSaveComplete
import com.baro.helpers.interfaces.OnCourseDeleted
import com.baro.helpers.interfaces.OnUserDataFound
import com.baro.helpers.interfaceweaks.OnCreatorCourseCredentialsLoad
import com.baro.models.Course
import com.baro.models.User
import com.baro.ui.create.CreateCourseSummaryFragment
import com.baro.ui.create.EditCourseSummaryFragment
import java.lang.ref.WeakReference
import java.nio.file.Paths
import java.util.*
import kotlin.collections.ArrayList


class AccountActivity : AppCompatActivity(), OnUserDataFound, OnCreatorCourseCredentialsLoad,
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

    @RequiresApi(Build.VERSION_CODES.O)
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
        val loadUserDataParams = AsyncHelpers.LoadUserData.TaskParams(user, this.contentResolver)
        val userRetrieveThumbnail = AsyncHelpers.LoadUserData(this)
        userRetrieveThumbnail.execute(loadUserDataParams)

    }

    override fun onBackPressed() {
        tellFragments()
        super.onBackPressed()
    }

    private fun tellFragments() {
        val fragments: List<Fragment> = supportFragmentManager.fragments
        for (f in fragments) {
            if (f != null && f is EditCourseSummaryFragment) {
                f.onBackPressed()
            }
        }
    }


    private fun configureCreateButton() {
        createButton = findViewById(R.id.btn_create)

        createButton.setOnClickListener {
            // TODO __PERMISSION_REFACTOR__

            val course = Course(UUID.randomUUID(), user)
            course.setCreationDate(System.currentTimeMillis())
            val createCourseSummaryFragment: CreateCourseSummaryFragment =
                CreateCourseSummaryFragment.newInstance(course)

            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container_peer_connection, createCourseSummaryFragment, null)
                .addToBackStack(AppTags.CREATE_COURSE_SUMMARY_FRAGMENT.name)
                .setReorderingAllowed(true)
                .commit()
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
        var coursePath = Paths.get(
            getExternalFilesDir(null).toString(),
            FileEnum.USER_DIRECTORY.key,
            FileEnum.COURSE_DIRECTORY.key
        )
        var params = AsyncHelpers.CreatorCourseCredentialsLoad.TaskParams(coursePath, user)
        AsyncHelpers.CreatorCourseCredentialsLoad(this).execute(params)
    }

    override fun onUserDataReturned(userData: AsyncHelpers.LoadUserData.LoadUserDataResponse?) {
        if (user != null) {
            val imageBmp = userData?.imageBmp
            if (imageBmp != null) {
                userThumbnailImageView.setImageBitmap(imageBmp)
            }
        }

    }

    override fun onCreatorCourseCredentialsLoad(courses: ArrayList<Pair<Course, Uri?>>) {
        this.courses = courses
        updateRecycleView()
    }

    private fun updateRecycleView() {
        courseAdapter?.notifyDataSetChanged()

        var weakReference = WeakReference<Context>(this)
        courseAdapter = CourseAdapter(weakReference, this.courses, this)
        courseRecycleView.adapter = courseAdapter
    }

    override fun notifyCourseSelected(course: Course) {
        // TODO __PERMISSION_REFACTOR_holder_

        val editCourseSummaryFragment: EditCourseSummaryFragment =
            EditCourseSummaryFragment.newInstance(course)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container_peer_connection, editCourseSummaryFragment, null)
            .addToBackStack(AppTags.EDIT_COURSE_SUMMARY_FRAGMENT.name)
            .setReorderingAllowed(true)
            .commit()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCourseDataReturned(result: Boolean?) {
        getCoursesFromFiles()
        courseAdapter?.notifyDataSetChanged()
    }

    override fun onCourseDeleted(courseDeleted: Course?) {
        var position = -1

        for (course in courses) {
            if (courseDeleted?.getCourseUUID() == course.first.getCourseUUID()) {
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
}