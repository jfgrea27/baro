package com.baro.helpers.interfaces

import java.io.File

interface OnVideoUriSaved {
    fun onVideoUriSaved(outputFile: File?)
}