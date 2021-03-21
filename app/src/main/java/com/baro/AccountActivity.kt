package com.baro

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView


class AccountActivity : AppCompatActivity() {
    private lateinit var accountButton: ImageButton
    private lateinit var followersButton: ImageButton
    private lateinit var settingsButton: ImageButton
    private lateinit var deleteLocalButton: ImageButton
    private lateinit var deleteRemoteButton: ImageButton
//    private lateinit val courseRecycleView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        configureAccountButton()
        configureFollowersButton()
        configureSettingsButton()
        configureDeleteLocalButton()
        configureDeleteRemoteButton()
        configureRecycleView()
    }

    private fun configureAccountButton() {
        accountButton = findViewById(R.id.btn_account)
        accountButton.setOnClickListener({ })
    }

    private fun configureFollowersButton() {
        followersButton = findViewById(R.id.btn_followers)
        followersButton.setOnClickListener(View.OnClickListener { v: View? -> })
    }

    private fun configureSettingsButton() {
        settingsButton = findViewById<ImageButton?>(R.id.btn_settings)
        settingsButton.setOnClickListener(View.OnClickListener { v: View? -> })
    }

    private fun configureDeleteLocalButton() {
        deleteLocalButton = findViewById<ImageButton?>(R.id.btn_delete_local)
        deleteLocalButton.setOnClickListener(View.OnClickListener { v: View? -> })
    }

    private fun configureDeleteRemoteButton() {
        deleteRemoteButton = findViewById<ImageButton?>(R.id.btn_delete_remote)
        deleteRemoteButton.setOnClickListener(View.OnClickListener { v: View? -> })
    }

    private fun configure() {
        accountButton = findViewById<ImageButton?>(R.id.btn_account)
        accountButton.setOnClickListener(View.OnClickListener { v: View? -> })
    }

    private fun configureRecycleView() {
        // TODO Implement this method
    }
}