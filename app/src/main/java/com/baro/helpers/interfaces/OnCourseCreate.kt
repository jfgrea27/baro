package com.baro.helpers.interfaces

import android.net.Uri
import com.baro.models.Course

interface OnCourseCreate {
    fun onCourseCreate(course: Pair<Course, Uri?>)
}