package com.baro.models


import java.net.URI
import java.time.LocalDate
import java.util.*

class Course // TODO discuss properties
(private val courseName: String?, private val courseUUID: UUID?, private val creator: User?) {
    private val updateDate: LocalDate? = null
    private val photoThumbnail: URI? = null
    private val soundThumbnail: URI? = null
    private val slides: ArrayList<Slide?>? = null

}