package com.baro.helpers.interfaces

import android.net.Uri

interface OnUserCredentialsSaveComplete {
    fun onUserCredentialsSaveDone(result: Boolean?)
    fun getUsername(): String
    fun getPath(): String
    fun getPhotoUri(): Uri?
}