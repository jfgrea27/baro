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

@Parcelize
data class
Course // TODO discuss properties
(private val courseUUID: UUID?, private val creator: User?, ) : Parcelable{
    private var courseName: String? = null
    private var languageEnum: LanguageEnum? = null
    private var categoryEnum: CategoryEnum? = null
    private var updateDate: LocalDate? = null
    private var photoThumbnail: File? = null
    private val slides: ArrayList<Slide?>? = null

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

    fun setThumbnailFile(file: File) {
        photoThumbnail = file
    }

    fun setUpdateDate(date: LocalDate) {
        updateDate = date
    }

}