package com.example.trustgate.presentation.features.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.trustgate.domain.model.UserRole
import com.example.trustgate.domain.model.VerificationStatus
import com.example.trustgate.presentation.features.auth.AuthViewModel
import com.example.trustgate.presentation.features.verification.VerificationViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.first

@Composable
fun SplashRoute(
    authViewModel: AuthViewModel,
    verificationViewModel: VerificationViewModel,
    onGoToVisitorHome: () -> Unit,
    onGoToGateHome: () -> Unit,
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

        // Si no se marcó recordar sesión o no hay usuario, se manda a Login
        if (!keep || user == null) {
            onGoToLogin()
            return@LaunchedEffect
        }

        val session = authViewModel.currentSession()

        // Si no hasy sesión se manda a login
        if (session == null) {
            onGoToLogin()
            return@LaunchedEffect
        }

        when (session.role) {
            // Cuando el rol del usuario es gate, se manda a su pantalla
            UserRole.GATE -> {
                onGoToGateHome()
            }

            // Cuando el rol del usuario es visitante, se manda a su pantalla
            UserRole.VISITOR -> {
                val state = verificationViewModel.refreshAndGetStatus()
                if (state == VerificationStatus.Completed) {
                    onGoToVisitorHome()
                } else {
                    onGoToLogin()
                }
            }
        }
    }
}