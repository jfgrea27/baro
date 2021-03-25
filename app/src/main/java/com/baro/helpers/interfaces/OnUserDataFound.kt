package com.baro.helpers.interfaces

import android.graphics.Bitmap

interface OnUserDataFound {
    fun onDataReturned(imageBmp: Bitmap?)
}