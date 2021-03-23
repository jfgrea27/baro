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
(private val courseName: String?, private val courseUUID: UUID?, private val creator: User?, private val languageEnum: LanguageEnum?, private val categoryEnum: CategoryEnum?) : Parcelable{
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

    fun getCourseLanguage(): LanguageEnum? {
        return languageEnum
    }

    fun getCourseCategory(): CategoryEnum? {
        return categoryEnum
    }

    fun setThumbnailFile(file: File) {
        photoThumbnail = file
    }

    fun setUpdateDate(date: LocalDate) {
        updateDate = date
    }
}