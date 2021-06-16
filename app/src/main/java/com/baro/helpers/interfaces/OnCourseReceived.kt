package com.baro.helpers.interfaces

interface OnCourseReceived {
    fun onCourseReceived(result: Boolean?)
    fun retrieveSize(courseSize: Long)
    fun sendProgress(currentSize: Int)
}