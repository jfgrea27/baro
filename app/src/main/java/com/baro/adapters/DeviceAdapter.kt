package com.baro.adapters

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.net.wifi.p2p.WifiP2pDevice
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.baro.R
import com.baro.models.Course
import java.lang.ref.WeakReference

class DeviceAdapter internal constructor(
    var context: WeakReference<Activity>,
    var devices: ArrayList<WifiP2pDevice>
    ): ArrayAdapter<WifiP2pDevice>(context.get()!!, R.layout.device_cell, devices){

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.get()?.layoutInflater
        val rowView = inflater?.inflate(R.layout.device_cell, null, true)

        val titleText = rowView?.findViewById(R.id.txt_device_cell) as TextView

        titleText.text = devices[position].deviceName

        return rowView
    }

}