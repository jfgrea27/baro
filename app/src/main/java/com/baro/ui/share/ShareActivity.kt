package com.baro.ui.share

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

import com.baro.R

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
        internetButton.setOnClickListener(View.OnClickListener { v: View? ->
            val intentUploadActivity = Intent(this@ShareActivity,
                    UploadActivity::class.java)
            startActivity(intentUploadActivity)
        })
    }

    private fun configureLocalButton() {
        localButton = findViewById<ImageButton?>(R.id.btn_local)
        localButton.setOnClickListener(View.OnClickListener { v: View? ->
            val intentSendReceiveActivity = Intent(this@ShareActivity,
                    SendReceiveActivity::class.java)
            startActivity(intentSendReceiveActivity)
        })
    }
}