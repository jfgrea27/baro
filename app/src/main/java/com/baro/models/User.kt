package com.baro.models


import java.net.URI
import java.util.*

class User // TODO discuss properties
(private val userUUID: UUID?, private val username: String?) {
    private val photoThumbnail: URI? = null
    private val followings: ArrayList<User?>? = null
    private val courses: ArrayList<Course?>? = null
    fun getUsername(): String? {
        return username
    }
    fun getUserUUID(): UUID? {
        return userUUID
    }
}