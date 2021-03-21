package com.baro.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

import com.baro.AccountActivity
import com.baro.R
import com.baro.ui.create.CreateActivity
import com.baro.ui.learn.LearnActivity
import com.baro.ui.share.ShareActivity

class MainActivity : AppCompatActivity() {
    private lateinit var accountButton: ImageButton
    private lateinit var shareButton: ImageButton
    private lateinit var createButton: ImageButton
    private lateinit var learnButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configureAccountButton()
        configureShareButton()
        configureCreateButton()
        configureLearnButton()
    }

    private fun configureLearnButton() {
        learnButton = findViewById(R.id.btn_learn)
        learnButton.setOnClickListener {
            val intentLearnActivity = Intent(
                    this@MainActivity,
                    LearnActivity::class.java)
            startActivity(intentLearnActivity)

        }
    }

    private fun configureAccountButton() {
        accountButton = findViewById(R.id.btn_account)
        accountButton.setOnClickListener {
            val intentAccountActivity = Intent(
                    this@MainActivity,
                    AccountActivity::class.java)
            startActivity(intentAccountActivity)
        }
    }

    private fun configureCreateButton() {
        createButton = findViewById(R.id.btn_create)
        createButton.setOnClickListener {
            val intentCreateCourse = Intent(
                    this@MainActivity,
                    CreateActivity::class.java)
            startActivity(intentCreateCourse)
        }
    }

    private fun configureShareButton() {
        shareButton = findViewById(R.id.btn_share)
        shareButton.setOnClickListener {
            val intentShareActivity = Intent(
                    this@MainActivity,
                    ShareActivity::class.java)
            startActivity(intentShareActivity)
        }
    }
}