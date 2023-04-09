package com.karyna.runningapp.auth

import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.karyna.core.data.RunningRepository
import com.karyna.core.domain.User
import com.karyna.feature.core.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: RunningRepository) : BaseViewModel() {
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun processGoogleIdToken(googleIdToken: String?) {
        when {
            googleIdToken != null -> {
                val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                firebaseAuth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val isNewUser = task.result.additionalUserInfo?.isNewUser ?: true
                            firebaseAuth.currentUser?.let { saveUser(it, isNewUser) }
                            navigateToApp()
                        } else {
                            // todo display a message to the user.
                            Timber.w(task.exception, "signInWithCredential:failure")
                        }
                    }
            }
            else -> {
                Timber.d("No ID token")
            }
        }
    }

    private fun saveUser(firebaseUser: FirebaseUser, isNewUser: Boolean) {
        with(firebaseUser) {
            if (email != null && displayName != null && photoUrl != null) {
                val user = User(
                    id = uid,
                    email = email!!,
                    name = displayName!!,
                    avatarUrl = photoUrl!!.toString(),
                    weight = null
                )
                viewModelScope.launch { repository.addUser(user, isNewUser) }
            }
        }
    }

    private fun navigateToApp() {
        navigate(AuthFragmentDirections.actionLoginFragmentToMap())
    }
}