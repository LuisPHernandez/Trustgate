package com.example.trustgate.features.verification.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.trustgate.features.verification.ui.VerificationShell
import com.example.trustgate.features.verification.ui.VerificationViewModel
import com.example.trustgate.features.verification.ui.consent.VerificationConsentScreen
import com.example.trustgate.features.verification.ui.photo.VerificationPhotoScreen

@Composable
fun VerificationNavigation(
    innerNavController: NavHostController = rememberNavController(),
    viewModel: VerificationViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onFinish: (Uri) -> Unit
) {
    VerificationShell {
        NavHost(
            navController = innerNavController,
            startDestination = VerificationInnerScreens.Consent.route
        ) {
            composable(VerificationInnerScreens.Consent.route) {
                VerificationConsentScreen(
                    checked = viewModel.consent,
                    onCheckedChange = viewModel::onConsentChange,
                    onContinueClick = {
                        if (viewModel.consent) innerNavController.navigate(VerificationInnerScreens.Photo.route)
                    },
                )
            }
            composable(VerificationInnerScreens.Photo.route) {
                VerificationPhotoScreen(
                    onContinue = { uri ->
                        onFinish(uri)
                    }
                )
            }
        }
    }
}