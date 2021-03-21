package com.baro.ui.share

import android.content.Intent
import android.os.Bundle
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
        sendButton.setOnClickListener {
            val intent = Intent(this, BluetoothSendActivity::class.java)
            startActivity(intent)
        }
    }
    private fun configureReceiveButton() {
        receiveButton = findViewById<ImageButton?>(R.id.btn_receive)
        receiveButton.setOnClickListener{
            val intent = Intent(this, BluetoothReceiveActivity::class.java)
            startActivity(intent)
        }

    }
}