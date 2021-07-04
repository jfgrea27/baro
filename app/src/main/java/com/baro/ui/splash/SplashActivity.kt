package com.baro.ui.splash

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

import com.baro.R
import com.baro.constants.AppTags
import com.baro.helpers.*
import com.baro.models.User
import com.baro.ui.main.MainActivity
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SplashActivity : AppCompatActivity() {

    private var progressBar: ProgressBar? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        configureProgressBar()
        if (savedInstanceState == null) {
            Handler(Looper.getMainLooper()).postDelayed({ getCurrentlyLoggingInUser() }, SPLASH_TIME_OUT.toLong())
        }
    }


    private fun configureProgressBar() {
        progressBar = findViewById(R.id.progress_bar)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentlyLoggingInUser() {

        runBlocking {
            launch {
                var user = AsyncHelpers().verifyUserCredentials(getExternalFilesDir(null))
                onUserLoginCheckDone(user)
            }
        }

    }

    companion object {
        const val SPLASH_TIME_OUT = 1000
    }


    private fun onUserLoginCheckDone(result: User?) {
        if (result != null) {

            val startMainActivity = Intent(
                    this,
                    MainActivity::class.java)

            startMainActivity.putExtra(AppTags.USER_OBJECT.name, result)

            startActivity(startMainActivity)
            finish()
        } else {
            Toast.makeText(this,
                    getString(R.string.toast_no_username_found),
                    Toast.LENGTH_LONG)
                    .show()

            progressBar!!.visibility = View.GONE


            supportFragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_peer_connection, SplashLoggingFragment::class.java, null)
                    .commit()
        }
    }
}
