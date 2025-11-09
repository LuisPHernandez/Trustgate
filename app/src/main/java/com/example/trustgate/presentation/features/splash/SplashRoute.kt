package com.example.trustgate.presentation.features.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.trustgate.domain.model.VerificationStatus
import com.example.trustgate.presentation.features.auth.AuthViewModel
import com.example.trustgate.presentation.features.verification.VerificationViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.first

@Composable
fun SplashRoute(
    authViewModel: AuthViewModel,
    verificationViewModel: VerificationViewModel,
    onGoToHome: () -> Unit,
    onGoToLogin: () -> Unit,
) {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }

    LaunchedEffect(Unit) {
        val keep = authViewModel.keepSignedIn.first()
        val user = FirebaseAuth.getInstance().currentUser
        val state = verificationViewModel.refreshAndGetStatus()
        if (keep && user != null && state == VerificationStatus.Completed) {
            onGoToHome()
        } else {
            onGoToLogin()
        }
    }
}