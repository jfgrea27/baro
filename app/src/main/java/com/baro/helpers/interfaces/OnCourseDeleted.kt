package com.baro.helpers.interfaces

import com.baro.models.Course

interface OnCourseDeleted {
    fun onCourseDeleted(result: Course?)
}