package com.karyna.runningapp.auth

import android.content.IntentSender
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.karyna.feature.core.utils.base.BaseFragment
import com.karyna.runningapp.databinding.FragmentAuthBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import com.karyna.feature.core.R as RCore

@AndroidEntryPoint
class AuthFragment : BaseFragment<FragmentAuthBinding, AuthViewModel>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAuthBinding =
        { layoutInflater, viewGroup, b -> FragmentAuthBinding.inflate(layoutInflater, viewGroup, b) }
    override val viewModel: AuthViewModel by viewModels()

    private var showOneTapUI = true
    private lateinit var oneTapClient: SignInClient

    private val oneTapSignInIntentResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result != null) {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                    val idToken = credential.googleIdToken
                    viewModel.processGoogleIdToken(idToken)
                } catch (e: ApiException) {
                    when (e.statusCode) {
                        CommonStatusCodes.CANCELED -> {
                            Timber.d("One-tap dialog was closed.")
                            showOneTapUI = false
                        }
                        CommonStatusCodes.NETWORK_ERROR -> {
                            Timber.d("One-tap encountered a network error.")
                        }
                        else -> {
                            Timber.d("Couldn't get credential from result." + " (" + e.message + ")")
                        }
                    }
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        oneTapClient = Identity.getSignInClient(requireActivity())
        binding.btnGoogleSignIn.setOnClickListener { auth() }
    }

    private fun auth() {
        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(RCore.string.auth_web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener { result ->
                try {
                    val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    oneTapSignInIntentResultLauncher.launch(intentSenderRequest)
                } catch (e: IntentSender.SendIntentException) {
                    Timber.e("Couldn't start One Tap UI: " + e.message)
                }
            }
            .addOnFailureListener { e ->
                // No saved credentials found
                Timber.d(e.message.orEmpty())
                if (showOneTapUI) oneTapClient.beginSignIn(signInRequest)
            }
    }
}