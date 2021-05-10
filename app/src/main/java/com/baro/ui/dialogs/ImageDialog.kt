package com.baro.ui.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatDialogFragment

import com.baro.R
import com.baro.constants.AppCodes


class ImageDialog(var onInputListener: OnInputListener?) : AppCompatDialogFragment() {
    private lateinit var gallery: ImageButton
    private lateinit var roll: ImageButton

    interface OnInputListener {
        open fun sendInput(choice: Int)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_image_chooser, container, false)
        gallery = view.findViewById(R.id.btn_folder)
        gallery.setOnClickListener {
            onInputListener?.sendInput(AppCodes.GALLERY_SELECTION.code)
            dialog?.dismiss()
        }
        roll = view.findViewById(R.id.btn_camera)
        roll.setOnClickListener {
            onInputListener?.sendInput(AppCodes.CAMERA_ROLL_SELECTION.code)
            dialog?.dismiss()
        }
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