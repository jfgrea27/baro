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
import androidx.recyclerview.widget.RecyclerView
import com.baro.R
import com.baro.models.Category
import com.baro.models.Course
import java.lang.ref.WeakReference

class CourseAdapter internal constructor(var context: WeakReference<Context>, var courses: ArrayList<Pair<Course, Uri>>) : RecyclerView.Adapter<CourseAdapter.ViewHolder>() {
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

        val courseImage = courses[position].second

        holder.courseCountry.setImageResource(courseLanguage?.getImageResource(context)!!)

        var string = ""

        for (category in courseCategory) {
            string += "$category "
        }
        holder.courseCategory.text = string
        holder.courseButton.setImageURI(courseImage)

        holder.courseButton.setOnClickListener(View.OnClickListener {
//            .isSelected = !category.isSelected
//
//            if (category.isSelected) {
//                selectedCategories.add(category)
//                holder.categoryText.setBackgroundResource(R.color.light_green)
//            } else {
//                selectedCategories.remove(category)
//                holder.categoryText.setBackgroundResource(R.color.white)
//            }


        })
    }

    override fun getItemCount(): Int {
        return courses.size
    }

    fun getItem(id: Int): Pair<Course, Uri> {
        return courses[id]
    }




    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var courseButton: ImageButton = itemView.findViewById(R.id.btn_course_thumbnail)
        var courseCountry: ImageView = itemView.findViewById(R.id.ic_language)
        var courseCategory: TextView = itemView.findViewById(R.id.txt_category)
    }

}