package com.baro.models


import android.os.Parcel
import android.os.Parcelable
import com.baro.constants.CategoryEnum
import com.baro.constants.LanguageEnum
import kotlinx.android.parcel.Parcelize
import java.io.File
import java.net.URI
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class
Course // TODO discuss properties
(private val courseUUID: UUID?, private val creator: User?, ) : Parcelable{
    private var courseName: String? = null
    private var languageEnum: LanguageEnum? = null
    private var categoryEnum: CategoryEnum? = null
    private var updateDate: LocalDate? = null
    internal var slides: ArrayList<Slide?>? = null

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

    fun getCourseLanguage(): LanguageEnum? {
        return languageEnum
    }

    fun setCourseLanguage(language :LanguageEnum) {
        languageEnum = language
    }
    fun getCourseCategory(): CategoryEnum? {
        return categoryEnum
    }

    fun setCourseCategory(category :CategoryEnum) {
        categoryEnum = category
    }

    fun getSlides(): ArrayList<Slide?>? {
        if(slides == null) {
            slides = ArrayList()
        }
        return slides
    }

}