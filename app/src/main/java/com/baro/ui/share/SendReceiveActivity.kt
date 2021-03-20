package com.baro.ui.share

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.baro.R


class SendReceiveActivity : AppCompatActivity() {

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
        sendButton.setOnClickListener(View.OnClickListener { v: View? ->
            val intentSendActivity = Intent(this@SendReceiveActivity,
                    BluetoothSendActivity::class.java)
            startActivity(intentSendActivity)
        })
    }

    private fun configureReceiveButton() {
        receiveButton = findViewById<ImageButton?>(R.id.btn_receive)
        receiveButton.setOnClickListener(View.OnClickListener { v: View? ->
            val intentReceiveActivity = Intent(this@SendReceiveActivity,
                    BluetoothReceiveActivity::class.java)
            startActivity(intentReceiveActivity)
        })
    }
}