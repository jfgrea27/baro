package com.baro.ui.share.p2p

import android.app.Activity
import androidx.fragment.app.Fragment
import android.net.wifi.p2p.WifiP2pDevice
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.annotation.RequiresApi
import com.baro.R
import com.baro.adapters.DeviceAdapter
import java.lang.ref.WeakReference


class WifiDirectPeerSendFragment : Fragment() {

    private lateinit var peerListView: ListView
    private lateinit var wifiDirectStatus: ImageView


    // Adapter
    private lateinit var adapter: DeviceAdapter
    private var devices = ArrayList<WifiP2pDevice>()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view =  inflater.inflate(R.layout.fragment_wifi_direct_send_connect, container, false)

        configurePeerListView(view)
        configureWifiDirectStatusImage(view)
        return view
    }

    private fun configureWifiDirectStatusImage(view: View) {
        wifiDirectStatus = view.findViewById(R.id.img_wifidirect_connection_status)
    }

    private fun configurePeerListView(view: View){
        peerListView = view.findViewById(R.id.rv_device_list)
        val weakActivity = WeakReference<Activity>(activity)
        adapter = DeviceAdapter(weakActivity, devices)
        peerListView.adapter = adapter

        peerListView.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            (activity as WifiDirectActivity).connectClientDevice(devices[position])
            }
     }


    companion object {

        @JvmStatic
        fun newInstance() =
            WifiDirectPeerSendFragment().apply {
            }
    }

    fun changeWifiDirectStatus(isConnected: Boolean) {
        if(isConnected) {
            wifiDirectStatus.setImageResource(R.drawable.ic_wifi)
        } else {
            wifiDirectStatus.setImageResource(R.drawable.ic_wifi_off)
        }
    }

    fun updateWifiP2PDeviceList(wifiP2pDeviceList: MutableCollection<WifiP2pDevice>) {
        for (device in wifiP2pDeviceList) {
            if (device !in devices) {
                devices.add(device)
            }
        }
        adapter.notifyDataSetChanged()
    }
}