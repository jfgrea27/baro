package com.baro.models


import com.baro.constants.CategoryEnum
import com.baro.constants.LanguageEnum
import java.net.URI
import java.time.LocalDate
import java.util.*

class Course // TODO discuss properties
(private val courseName: String?, private val courseUUID: UUID?, private val creator: User?, private val languageEnum: LanguageEnum?, private val categoryEnum: CategoryEnum?) {
    private val updateDate: LocalDate? = null
    private val photoThumbnail: URI? = null
    private val soundThumbnail: URI? = null
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

}