package com.baro.ui.splash

import android.app.Fragment
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible

import com.baro.R
import com.baro.constants.AppTags
import com.baro.constants.FileEnum
import com.baro.constants.JSONEnum
import com.baro.helpers.FileHelper
import com.baro.helpers.JSONHelper
import com.baro.models.User
import com.baro.ui.main.MainActivity

import org.json.JSONException
import java.nio.file.Paths
import java.util.*

class SplashActivity : AppCompatActivity() {
    private var progressBar: ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        configureProgressBar()
        if (savedInstanceState == null) {
            Handler().postDelayed(Runnable { getCurrentlyLoggingInUser() } as Runnable, SPLASH_TIME_OUT.toLong())
        }
    }


    private fun configureProgressBar() {
        progressBar = findViewById<ProgressBar?>(R.id.progress_bar)
    }

    private fun getCurrentlyLoggingInUser() {
        val userCredentialsTask = UserCredentialsTask()
        userCredentialsTask.execute()
    }

    private inner class UserCredentialsTask : AsyncTask<Void?, Void?, User?>() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        override fun doInBackground(vararg voids: Void?): User? {
            val userMetaDataPath = Paths.get(getExternalFilesDir(null).toString(),
                    FileEnum.USER_DIRECTORY.key,
                    FileEnum.META_DATA_FILE.key)
            val userMetaDataFile = FileHelper.getFileAtPath(userMetaDataPath)
            if (userMetaDataFile != null) {
                val contentMetaData = FileHelper.readFile(userMetaDataFile)
                val jsonMetaData = contentMetaData?.let { JSONHelper.createJSONFromString(it) }
                return try {
                    User(
                            UUID.fromString(jsonMetaData?.get(JSONEnum.USER_UUID_KEY.key) as String),
                            jsonMetaData[JSONEnum.USER_NAME_KEY.key] as String)
                } catch (e: JSONException) {
                    null
                }
            }
            return null
        }

        override fun onPostExecute(result: User?) {
            if (result != null) {
                
                val startMainActivity = Intent(
                        this@SplashActivity,
                        MainActivity::class.java)
                
                startMainActivity.putExtra(AppTags.USER_OBJECT.name, result)
                
                startActivity(startMainActivity)
                finish()
            } else {
                Toast.makeText(this@SplashActivity,
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

    companion object {
        const val SPLASH_TIME_OUT = 1000
    }
}
