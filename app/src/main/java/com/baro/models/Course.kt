package com.baro.models


import android.os.Parcelable
import com.baro.constants.CategoryEnum
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class
Course // TODO discuss properties
(private val courseUUID: UUID?, private val creator: User?, ) : Parcelable{
    private var courseName: String? = null
    private var country: Country? = null
    private var categoryEnum: CategoryEnum? = null
    private var updateDate: LocalDate? = null
    private var slides: ArrayList<Slide> = ArrayList()

    fun getCourseUUID(): UUID? {
        return courseUUID
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

    fun getCourseCategory(): CategoryEnum? {
        return categoryEnum
    }

    fun setCourseCategory(category :CategoryEnum) {
        categoryEnum = category
    }

    fun getSlides(): ArrayList<Slide> {
        return slides
    }

}