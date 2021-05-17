package com.baro.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.baro.R
import com.baro.constants.AppTags
import com.baro.helpers.*
import com.baro.helpers.interfaces.OnUserLoginCheckComplete
import com.baro.models.User
import com.baro.ui.main.MainActivity

class SplashActivity : AppCompatActivity(), OnUserLoginCheckComplete {

    private var progressBar: ProgressBar? = null

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

    private fun getCurrentlyLoggingInUser() {
        val userCredentialsTask = AsyncHelpers.VerifyUserCredentials(this)
        userCredentialsTask.execute(getExternalFilesDir(null))
    }

    companion object {
        const val SPLASH_TIME_OUT = 1000
    }


    override fun onUserLoginCheckDone(result: User?) {
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
                    .add(R.id.fragment_container_view, SplashLoggingFragment::class.java, null)
                    .commit()
        }
    }
}
