package com.baro.ui.splash


import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import com.google.firebase.auth.FirebaseAuth
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


    @RequiresApi(Build.VERSION_CODES.M)
    private fun onUserLoginCheckDone(result: User?) {
        if (result != null) {

            if (isOnline(this.applicationContext)) {
                if (FirebaseAuth.getInstance().currentUser?.uid == null) {
                    supportFragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .add(R.id.fragment_container_peer_connection,
                            SplashLoggingFirebaseFragment::class.java, null)
                        .commit()
                }
            }
            val startMainActivity = Intent(
                this,
                MainActivity::class.java)

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

    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context?): Boolean {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                        return true
                    }
                }
            }
        }
        return false
    }
}
