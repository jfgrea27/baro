package com.baro.models


import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*


@Parcelize
data class Slide(val slideUUID: UUID) : Parcelable {
    private var video: Uri? = null
    private var course: Course? = null

    fun setCourse(course: Course) {
        this.course = course
    }
    fun getCourse() : Course? {
        return this.course
    }

    override fun toString(): String {
        return slideUUID.toString()
    }

    fun getVideoUri() : Uri? {
        return video
    }

    fun setVideoUri(uri: Uri?) {
        video = uri
    }
}