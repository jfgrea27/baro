package com.baro.ui.splash

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.baro.R
import com.baro.ui.main.MainActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@RequiresApi(api = Build.VERSION_CODES.N)
class SplashLoggingFirebaseFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private var customToken: String? = null

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { _ ->
        this.onSignInResult()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createSignInIntent()
    }

    private fun createSignInIntent() {

        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build())

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }


    private fun onSignInResult() {
        val startMainActivity = Intent(
            activity,
            MainActivity::class.java)
        // Perhaps use UserCredentialsTask From SplashActivity to get the credentials once created
        startActivity(startMainActivity)
        requireActivity().finish()
    }


}


