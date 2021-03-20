package com.baro.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

import com.baro.AccountActivity
import com.baro.R
import com.baro.ui.create.CreateActivity
import com.baro.ui.share.ShareActivity

class MainActivity : AppCompatActivity() {
    private lateinit var accountButton: ImageButton
    private lateinit var shareButton: ImageButton
    private lateinit var createButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configureAccountButton()
        configureShareButton()
        configureCreateButton()
    }

    private fun configureAccountButton() {
        accountButton = findViewById<ImageButton?>(R.id.btn_account)
        accountButton.setOnClickListener(View.OnClickListener { v: View? ->
            val intentAccountActivity = Intent(
                    this@MainActivity,
                    AccountActivity::class.java)
            startActivity(intentAccountActivity)
        })
    }

    private fun configureCreateButton() {
        createButton = findViewById<ImageButton?>(R.id.btn_create)
        createButton.setOnClickListener(View.OnClickListener { v: View? ->
            val intentCreateCourse = Intent(
                    this@MainActivity,
                    CreateActivity::class.java)
            startActivity(intentCreateCourse)
        })
    }

    private fun configureShareButton() {
        shareButton = findViewById<ImageButton?>(R.id.btn_share)
        shareButton.setOnClickListener(View.OnClickListener { v: View? ->
            val intentShareActivity = Intent(
                    this@MainActivity,
                    ShareActivity::class.java)
            startActivity(intentShareActivity)
        })
    }
}