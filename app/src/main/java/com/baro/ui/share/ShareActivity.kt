package com.baro.ui.share

import android.content.Intent
import android.os.Bundle
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
        internetButton.setOnClickListener{
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        }
    }

    private fun configureLocalButton() {
        localButton = findViewById(R.id.btn_local)
        localButton.setOnClickListener{
            val intent = Intent(this, SendReceiveActivity::class.java)
            startActivity(intent)
        }


    }
}