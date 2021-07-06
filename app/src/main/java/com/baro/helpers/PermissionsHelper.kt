package com.baro.helpers

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.baro.constants.PermissionsEnum
import java.lang.ref.WeakReference

object PermissionsHelper {
    val REQUEST_ID_MULTIPLE_PERMISSIONS = 1

    fun checkAndRequestPermissions(weakReference: WeakReference<Activity>, action: PermissionsEnum): Boolean {
        val permissionsToCheck = action.permissions
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        for (permission in permissionsToCheck) {
            val result = ContextCompat.checkSelfPermission(weakReference.get()!!, permission)
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission)
            }
        }

        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(weakReference.get()!!, listPermissionsNeeded.toTypedArray(), REQUEST_ID_MULTIPLE_PERMISSIONS)
            return false
        }
        return true
    }

}

