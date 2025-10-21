package com.example.trustgate.presentation.features.verification.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.trustgate.presentation.features.verification.ui.VerificationShell
import com.example.trustgate.presentation.features.verification.ui.consent.VerificationConsentScreen
import com.example.trustgate.presentation.features.verification.ui.photo.VerificationPhotoScreen
import com.example.trustgate.presentation.features.verification.ui.VerificationViewModel

@Composable
fun VerificationNavigation(
    innerNavController: NavHostController = rememberNavController(),
    viewModel: VerificationViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onFinish: () -> Unit
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
                    onContinue = onFinish
                )
            }
        }
    }
}