package com.example.trustgate.presentation.features.verification.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.trustgate.data.verification.VerificationRepositoryImpl
import com.example.trustgate.data.verification.simulated.VerificationSimulatedDataSource
import com.example.trustgate.presentation.features.verification.ui.VerificationShell
import com.example.trustgate.presentation.features.verification.ui.consent.VerificationConsentScreen
import com.example.trustgate.presentation.features.verification.ui.photo.VerificationPhotoScreen
import com.example.trustgate.presentation.features.verification.viewmodel.VerificationViewModel
import com.example.trustgate.presentation.features.verification.viewmodel.VerificationViewModelFactory

@Composable
fun VerificationNavigation(
    innerNavController: NavHostController = rememberNavController(),
    onFinish: () -> Unit
) {
    val repo = remember { VerificationRepositoryImpl(VerificationSimulatedDataSource()) }
    val vm: VerificationViewModel = viewModel(factory = VerificationViewModelFactory(repo))

    VerificationShell(
        viewModel = vm,
    ) {
        NavHost(
            navController = innerNavController,
            startDestination = VerificationInnerScreens.Consent.route
        ) {
            composable(VerificationInnerScreens.Consent.route) {
                VerificationConsentScreen(
                    viewModel = vm,
                    onContinueClick = {
                        if (vm.consent) innerNavController.navigate(VerificationInnerScreens.Photo.route)
                    },
                )
            }
            composable(VerificationInnerScreens.Photo.route) {
                VerificationPhotoScreen(
                    viewModel = vm,
                    onContinue = onFinish
                )
            }
        }
    }
}