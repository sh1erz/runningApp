package com.karyna.runningapp.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.karyna.feature.core.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : BaseViewModel() {
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun processGoogleIdToken(googleIdToken: String?) {
        when {
            googleIdToken != null -> {
                val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                firebaseAuth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            navigateToApp()
                        } else {
                            // todo display a message to the user.
                            Timber.w(task.exception, "signInWithCredential:failure")
                        }
                    }
            }
            else -> {
                Timber.d("No ID token or password!")
            }
        }
    }

    private fun navigateToApp() {
        navigate(AuthFragmentDirections.actionLoginFragmentToMap())
    }
}