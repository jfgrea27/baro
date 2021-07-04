package com.baro.helpers.interfaces

interface OnCourseReceived {
    fun onCourseReceived(result: Boolean?)
    fun setProgressCourseSize(courseSize: Long)
    fun setProgress(currentSize: Int)
}