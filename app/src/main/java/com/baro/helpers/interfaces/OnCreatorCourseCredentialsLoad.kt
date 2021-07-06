package com.baro.helpers.interfaces

import android.net.Uri
import com.baro.models.Course

interface OnCreatorCourseCredentialsLoad {
    fun onCreatorCourseCredentialsLoad(courses:ArrayList<Pair<Course, Uri?>>)

}