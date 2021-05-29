package com.baro.ui.share

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pInfo
import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.baro.R
import com.baro.constants.AppCodes
import com.baro.constants.AppTags


class WifiDirectActivity : AppCompatActivity(), WifiP2pManager.ConnectionInfoListener {

    // WifiDirect
    private val manager: WifiP2pManager? by lazy(LazyThreadSafetyMode.NONE) {
        getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager?
    }
    private var channel: WifiP2pManager.Channel? = null
    private var receiver: BroadcastReceiver? = null

    private var isReceiving: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wifi_direct)

        getUserIntent()
        configureView()

        initWifiP2P()
        discoverPeers()
    }

    private fun getUserIntent() {
        val userIntent = intent.extras?.get(AppTags.WIFIP2P_INTENT.name)
        isReceiving = userIntent != AppCodes.WIFIP2P_PEER_SEND.code
    }


    private fun configureView() {
        if (isReceiving) {
            val peerReceiveFragment: WifiDirectPeerReceiveFragment =
                WifiDirectPeerReceiveFragment.newInstance()

            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container_view, peerReceiveFragment, null)
                .setReorderingAllowed(true)
                .commit()
        } else {
            val peerSendFragment: WifiDirectPeerSendFragment =
                WifiDirectPeerSendFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container_view, peerSendFragment, null)
                .setReorderingAllowed(true)
                .commit()
        }
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
                Toast.makeText(applicationContext, "DEBUG: Cannot discover peers", Toast.LENGTH_LONG).show()
            }
        })

    }

    private fun initWifiP2P() {
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

        if (!isReceiving) {
            // Stops connection between devices
            manager?.removeGroup(channel, object : WifiP2pManager.ActionListener {
                override fun onFailure(reasonCode: Int) {
                    Toast.makeText(
                        applicationContext,
                        "DEBUG: Could not disconnect devices Reason :$reasonCode", Toast.LENGTH_LONG
                    ).show()

                }

                override fun onSuccess() {
                    Toast.makeText(
                        applicationContext,
                        "DEBUG: Device disconnected", Toast.LENGTH_LONG
                    ).show()
                }

            })
        }
    }


    // Notify WifiDirectPeerConnectFragment
    fun wifiDirectStatusUpdate(wifiDirectConnected: Boolean) {
        if (isReceiving) {
            val wifiDirectPeerReceiveFragment = supportFragmentManager
                .findFragmentById(R.id.fragment_container_view) as WifiDirectPeerReceiveFragment?
            wifiDirectPeerReceiveFragment?.changeWifiDirectStatus(wifiDirectConnected)
        } else {
            val wifiDirectPeerSendFragment = supportFragmentManager
                .findFragmentById(R.id.fragment_container_view) as WifiDirectPeerSendFragment?
            wifiDirectPeerSendFragment?.changeWifiDirectStatus(wifiDirectConnected)
        }
    }

    fun updateWifiP2PDeviceList(wifiP2pDeviceList: MutableCollection<WifiP2pDevice>) {
        if (!isReceiving) {
            val wifiDirectPeerSendFragment = supportFragmentManager
                .findFragmentById(R.id.fragment_container_view) as WifiDirectPeerSendFragment
            wifiDirectPeerSendFragment.updateWifiP2PDeviceList(wifiP2pDeviceList)
        }
    }


    fun connectDevice(device: WifiP2pDevice) {
        val config = WifiP2pConfig()

        config.deviceAddress = device.deviceAddress
        channel?.also { channel ->
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
            if (!isReceiving) {
                manager?.createGroup(channel, object : WifiP2pManager.ActionListener {
                    override fun onSuccess() {
                        Toast.makeText(applicationContext, "DEBUG: Connected to " + device.deviceName, Toast.LENGTH_LONG).show()
                    }

                    override fun onFailure(reason: Int) {
                        Toast.makeText(applicationContext, "DEBUG: No connection established", Toast.LENGTH_LONG).show()

                    }
                })
            }


//            manager?.connect(channel, config, object : WifiP2pManager.ActionListener {
//
//                override fun onSuccess() {
//                    Toast.makeText(applicationContext, "DEBUG: Connected to " + device.deviceName, Toast.LENGTH_LONG).show()
//                }
//
//                override fun onFailure(reason: Int) {
//                    Toast.makeText(applicationContext, "DEBUG: No connection established", Toast.LENGTH_LONG).show()
//                }
//            })
        }
    }

    override fun onConnectionInfoAvailable(p0: WifiP2pInfo?) {
        Toast.makeText(applicationContext, "DEBUG: No connection established", Toast.LENGTH_LONG).show()
    }

}