package com.baro.helpers.interfaceweaks

import android.net.Uri
import com.baro.models.Course

interface OnCreatorCourseCredentialsLoad {
    fun onCreatorCourseCredentialsLoad(courses:ArrayList<Pair<Course, Uri>>)

}