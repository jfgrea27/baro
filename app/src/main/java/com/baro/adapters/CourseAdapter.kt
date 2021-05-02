package com.baro.adapters

import android.content.Context
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.net.toFile
import androidx.recyclerview.widget.RecyclerView
import com.baro.R
import com.baro.models.Course
import java.lang.ref.WeakReference

class CourseAdapter internal constructor(var context: WeakReference<Context>, var courses: ArrayList<Pair<Course, Uri>>, var onCourseSelected: OnCourseSelected) : RecyclerView.Adapter<CourseAdapter.ViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(context.get())

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = mInflater.inflate(R.layout.course_cell, parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {

        val courseLanguage = courses[position].first.getCourseCountry()
        val courseCategory = courses[position].first.getCourseCategory()
        val courseTitle = courses[position].first.getCourseName()

        val courseImage = courses[position].second
        val courseImageFile = courseImage.toFile()
        if (courseImageFile.length() > 0) {
            holder.courseButton.setImageURI(courseImage)
        }

        if (courseLanguage?.getIsoCode() == null) {
            holder.courseCountry.setImageResource(R.drawable.ic_flag)
        } else {
            holder.courseCountry.setImageResource(courseLanguage.getImageResource(context)!!)

        }

        var categories = ""

        for (category in courseCategory) {
            categories += "$category "
        }

        if (categories == "") {
            holder.courseCategory.text = context.get()?.resources?.getString(R.string.no_category)
        } else {
            holder.courseCategory.text = categories
        }

        holder.courseTitle.text = courseTitle
        holder.courseButton.setOnClickListener(View.OnClickListener {
            onCourseSelected.notifyCourseSelected(courses[position].first)
        })
    }

    override fun getItemCount(): Int {
        return courses.size
    }

    fun getItem(id: Int): Pair<Course, Uri> {
        return courses[id]
    }


    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var courseTitle: TextView = itemView.findViewById(R.id.txt_course_title)
        var courseButton: ImageButton = itemView.findViewById(R.id.btn_course_thumbnail)
        var courseCountry: ImageView = itemView.findViewById(R.id.ic_language)
        var courseCategory: TextView = itemView.findViewById(R.id.txt_category)
    }

    public interface OnCourseSelected { fun notifyCourseSelected(course: Course)}
}