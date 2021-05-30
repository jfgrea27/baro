package com.baro.ui.share.p2p

import androidx.fragment.app.Fragment
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import com.baro.R


class WifiDirectPeerReceiveFragment : Fragment() {

    private lateinit var wifiDirectStatus: ImageView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view =  inflater.inflate(R.layout.fragment_wifi_direct_receive_connext, container, false)

        configureWifiDirectStatusImage(view)
        return view
    }

    private fun configureWifiDirectStatusImage(view: View) {
        wifiDirectStatus = view.findViewById(R.id.img_wifidirect_connection_status)

    }

    companion object {

        @JvmStatic
        fun newInstance() =
            WifiDirectPeerReceiveFragment().apply {
            }
    }

    fun changeWifiDirectStatus(isConnected: Boolean) {
        if(isConnected) {
            wifiDirectStatus.setImageResource(R.drawable.ic_wifi)
        } else {
            wifiDirectStatus.setImageResource(R.drawable.ic_wifi_off)
        }
    }

}