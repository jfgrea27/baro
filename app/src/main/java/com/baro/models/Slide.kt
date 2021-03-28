package com.baro.models


import android.net.Uri
import java.net.URI
import java.util.*

class Slide(val slideUUID: UUID, val course: Course) {
    private var video: Uri? = null


    fun getVideoUri() : Uri? {
        return video
    }
}