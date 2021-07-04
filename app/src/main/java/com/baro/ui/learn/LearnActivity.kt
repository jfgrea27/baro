package com.baro.ui.learn

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baro.R
import com.baro.adapters.CourseAdapter
import com.baro.constants.AppTags
import com.baro.constants.FileEnum
import com.baro.constants.PermissionsEnum
import com.baro.helpers.AsyncHelpers
import com.baro.helpers.PermissionsHelper
import com.baro.models.Course
import kotlinx.coroutines.runBlocking
import java.lang.ref.WeakReference
import java.nio.file.Paths

class LearnActivity : AppCompatActivity(), CourseAdapter.OnCourseSelected, AsyncHelpers.OnCourseDeleted {
    private lateinit var courseRecycleView: RecyclerView

    // Model
    private var courseAdapter: CourseAdapter? = null
    private lateinit var courses: ArrayList<Pair<Course, Uri?>>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn)
        configureRecycleView()

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
        val coursesPath = Paths.get(
            getExternalFilesDir(null).toString(),
            FileEnum.LEARN_DIRECTORY.key,
        )
        runBlocking {
            run {
                val courses = AsyncHelpers().loadCoursesMetadataFromJson(coursesPath)
                this@LearnActivity.courses = courses
                updateRecycleView()
            }
        }

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

            val readCourseSummaryFragment: ReadCourseSummaryFragment =
                ReadCourseSummaryFragment.newInstance(course)

            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container_peer_connection, readCourseSummaryFragment, null)
                .addToBackStack(AppTags.EDIT_COURSE_SUMMARY_FRAGMENT.name)
                .setReorderingAllowed(true)
                .commit()
        }
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

}