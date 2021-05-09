package com.baro.ui.account

import com.baro.models.Course

interface OnDeleteCourse {
    fun onDeleteCourse(course: Course)
}