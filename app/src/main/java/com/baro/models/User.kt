package com.baro.models


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.File
import java.util.*


@Parcelize
data class User
(private val userUUID: UUID, private val username: String, private var thumbnail: File? = null
) : Parcelable {

    fun getUsername(): String {
        return username
    }
    fun getUserUUID(): UUID {
        return userUUID
    }
    fun getThumbnailFile(): File?  {
        return thumbnail
    }

    fun setThumbnailPath(thumbnailPath: String) {
        thumbnail = File(thumbnailPath)
    }

    private val courses: ArrayList<Course?>? = null
    private val followings: ArrayList<User?>? = null
}
