package com.baro.models


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.File
import java.net.URI
import java.util.*


@Parcelize
data class User
(private val userUUID: UUID, private val username: String) : Parcelable {
    private val photoThumbnail: File? = null
    fun getUsername(): String {
        return username
    }
    fun getUserUUID(): UUID {
        return userUUID
    }
    fun getPhotoThumbnailFile(): File?  {
        return photoThumbnail
    }

    private val courses: ArrayList<Course?>? = null
    private val followings: ArrayList<User?>? = null
}
