package com.baro.dialogs

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatDialogFragment

import com.baro.R
import com.baro.constants.AppCodes
import com.baro.constants.PermissionsEnum
import com.baro.helpers.PermissionsHelper
import java.lang.ref.WeakReference


class ImageDialog(var onInputListener: OnInputListener?) : AppCompatDialogFragment() {
    private lateinit var gallery: ImageButton
    private lateinit var roll: ImageButton

    interface OnInputListener {
        open fun sendInput(choice: Int)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_image_chooser, container, false)
        gallery = view.findViewById<ImageButton?>(R.id.btn_folder)
        gallery.setOnClickListener(View.OnClickListener {
            val weakReference = WeakReference<Activity>(this.activity)
            if (PermissionsHelper.checkAndRequestPermissions(weakReference, PermissionsEnum.GALLERY_SELECTION)) {
                onInputListener?.sendInput(AppCodes.GALLERY_SELECTION.code)
                dialog?.dismiss()
            }

        })
        roll = view.findViewById<ImageButton?>(R.id.btn_camera)
        roll.setOnClickListener(View.OnClickListener {
            val weakReference = WeakReference<Activity>(this.activity)
            if (PermissionsHelper.checkAndRequestPermissions(weakReference, PermissionsEnum.CAMERA_ROLL_SELECTION)) {
                onInputListener?.sendInput(AppCodes.CAMERA_ROLL_SELECTION.code)
                dialog?.dismiss()
            }

        })
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            onInputListener = activity as OnInputListener?
        } catch (e: ClassCastException) {
        }
    }

}