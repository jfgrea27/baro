package com.baro.ui.share

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.baro.R


class WifiDirectActivity : AppCompatActivity() {


    private lateinit var sendButton: ImageButton
    private lateinit var receiveButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_wifi_direct)

        configureBaseFragment()
        activateWifi()
        discoverPeers()
    }

    private fun configureBaseFragment() {
        val peerConnectFragment: WifiDirectPeerConnectFragment =
            WifiDirectPeerConnectFragment.newInstance()

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container_view, peerConnectFragment, null)
            .setReorderingAllowed(true)
            .commit()
    }

    private fun discoverPeers() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        manager?.discoverPeers(channel, object : WifiP2pManager.ActionListener {

            override fun onSuccess() {
                Toast.makeText(applicationContext, "DEBUG: Discovering peers", Toast.LENGTH_LONG).show()
            }

            override fun onFailure(reasonCode: Int) {
                Toast.makeText(applicationContext, "DEBUG: annot discover peers", Toast.LENGTH_LONG).show()
            }
        })

    }

    // WifiDirect
    private val manager: WifiP2pManager? by lazy(LazyThreadSafetyMode.NONE) {
        getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager?
    }

    var channel: WifiP2pManager.Channel? = null
    var receiver: BroadcastReceiver? = null

    private fun activateWifi() {
        channel = manager?.initialize(this, mainLooper, null)
        channel?.also { channel ->
            receiver = manager?.let { WiFiDirectBroadcastReceiver(it, channel, this) }
        }
    }

    private val intentFilter = IntentFilter().apply {
        addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
    }


    /* register the broadcast receiver with the intent values to be matched */
    override fun onResume() {
        super.onResume()
        receiver?.also { receiver ->
            registerReceiver(receiver, intentFilter)
        }
    }

    /* unregister the broadcast receiver */
    override fun onPause() {
        super.onPause()
        receiver?.also { receiver ->
            unregisterReceiver(receiver)
        }
    }


    // Notify WifiDirectPeerConnectFragment
    fun wifiDirectStatusUpdate(wifiDirectConnected: Boolean) {
        val wifiDirectPeerConnectFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container_view) as WifiDirectPeerConnectFragment

        wifiDirectPeerConnectFragment.changeWifiDirectStatus(wifiDirectConnected)
    }

    fun updateWifiP2PDeviceList(wifiP2pDeviceList: MutableCollection<WifiP2pDevice>) {
        val wifiDirectPeerConnectFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container_view) as WifiDirectPeerConnectFragment

        val devices = ArrayList<WifiP2pDevice>()

        for (device in wifiP2pDeviceList) {
            devices.add(device)
        }
        wifiDirectPeerConnectFragment.updateWifiP2PDeviceList(devices)
    }


    fun connectDevice(device: WifiP2pDevice) {
        Toast.makeText(applicationContext, "Connecting to " + device.deviceName, Toast.LENGTH_LONG).show()
    }

}