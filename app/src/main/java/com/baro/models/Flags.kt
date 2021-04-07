package com.baro.models

import android.content.res.Resources
import com.baro.R
import java.lang.ref.WeakReference

class Flags (var weakReference: WeakReference<Resources>) {

    fun getFlags() {
        val recourseList= weakReference.get()?.getStringArray(R.array.Flags)
    }

    fun getImageResource() {}
}