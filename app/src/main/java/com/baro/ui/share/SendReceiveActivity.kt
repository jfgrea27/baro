package com.baro.ui.share

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.baro.R
import com.baro.constants.AppCodes
import com.baro.constants.AppTags


class SendReceiveActivity : AppCompatActivity(){


    private lateinit var sendButton: ImageButton
    private lateinit var receiveButton: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_receive)
        configureSendButton()
        configureReceiveButton()
    }

    private fun configureSendButton() {
        sendButton = findViewById<ImageButton?>(R.id.btn_send)
        sendButton.setOnClickListener {
            val startWifiDirectActivity = Intent(
                this,
                WifiDirectActivity::class.java)

            startWifiDirectActivity.putExtra(AppTags.WIFIP2P_INTENT.name, AppCodes.WIFIP2P_PEER_SEND.code)
            startActivity(startWifiDirectActivity)
        }
    }
    private fun configureReceiveButton() {
        receiveButton = findViewById<ImageButton?>(R.id.btn_receive)
        receiveButton.setOnClickListener{
            val startWifiDirectActivity = Intent(
                this,
                WifiDirectActivity::class.java)
            startWifiDirectActivity.putExtra(AppTags.WIFIP2P_INTENT.name, AppCodes.WIFIP2P_PEER_RECEIVE.code)
            startActivity(startWifiDirectActivity)
        }

    }

}