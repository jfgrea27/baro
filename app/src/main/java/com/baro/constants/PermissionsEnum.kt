package com.baro.constants

import android.Manifest

enum class PermissionsEnum(val permissions: Array<String>){
    CAMERA_ROLL_SELECTION(arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)),
    GALLERY_SELECTION(arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)),
    CREATE_COURSE_SELECTION(arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)),
    READ_COURSE(arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE))
}