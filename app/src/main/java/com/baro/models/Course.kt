package com.baro.models


import android.os.Parcelable
import com.baro.constants.CategoryEnum
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class Course
(private val courseUUID: UUID?, private val creator: User?, ) : Parcelable, Comparable<Course>{
    private var courseName: String? = null
    private var country: Country? = null
    private var categories = ArrayList<Category>()
    private var creationDate: Long? = null
    private var slides: ArrayList<Slide> = ArrayList()

    fun getCourseUUID(): UUID? {
        return courseUUID
    }
    fun getCreationDate(): Long? {
        return creationDate
    }
    fun setCreationDate(timestamp: Long) {
        creationDate = timestamp
    }

    fun getCreator(): User? {
        return creator
    }

    fun getCourseName(): String? {
        return courseName
    }

    fun setCourseName(name: String) {
        courseName = name
    }

    fun getCourseCountry(): Country? {
        return country
    }

    fun setCourseCountry(country :Country?) {
        this.country = country
    }

    fun getCourseCategory(): ArrayList<Category> {
        return categories
    }

    fun setCourseCategory(category :ArrayList<Category> ) {
        categories = category
    }

    fun getSlides(): ArrayList<Slide> {
        return slides
    }

    override fun compareTo(other: Course): Int {
        return getCreationDate()!!.compareTo(other.getCreationDate()!!)
    }

}