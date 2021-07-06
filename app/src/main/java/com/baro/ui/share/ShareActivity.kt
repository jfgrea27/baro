package com.baro.ui.share

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

import com.baro.R
import com.baro.constants.AppCodes
import com.baro.constants.AppTags
import com.baro.constants.PermissionsEnum
import com.baro.helpers.PermissionsHelper
import com.baro.ui.share.firebase.FirebaseUploadActivity
import com.baro.ui.share.p2p.WifiDirectActivity
import java.lang.ref.WeakReference

class ShareActivity : AppCompatActivity() {
    private lateinit var internetButton: ImageButton
    private lateinit var localButton: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        configureInternetButton()
        configureLocalButton()
    }

    private fun configureInternetButton() {
        internetButton = findViewById(R.id.btn_internet)
        internetButton.setOnClickListener {
            val intent = Intent(this, FirebaseUploadActivity::class.java)
            startActivity(intent)
        }
    }

    private fun configureLocalButton() {
        localButton = findViewById(R.id.btn_local)
        localButton.setOnClickListener {
            if (PermissionsHelper.checkAndRequestPermissions(
                    WeakReference<Activity>(this),
                    PermissionsEnum.WIFI_DIRECT
                )
            ) {
                val startWifiActivity = Intent(
                    this,
                    WifiDirectActivity::class.java
                )

                startWifiActivity.putExtra(
                    AppTags.WIFIP2P_INTENT.name,
                    AppCodes.WIFIP2P_PEER_RECEIVE.code
                )
                startActivity(startWifiActivity)
            }


        }


    }
}