package com.example.trustgate.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.trustgate.core.common.RepositoryProvider
import com.example.trustgate.data.gate.GateRepositoryImpl
import com.example.trustgate.data.gate.simulated.GateSimulatedDataSource
import com.example.trustgate.domain.model.VerificationStatus
import com.example.trustgate.presentation.features.auth.AuthViewModel
import com.example.trustgate.presentation.features.auth.AuthViewModelFactory
import com.example.trustgate.presentation.features.home.ui.HomeScreen
import com.example.trustgate.presentation.features.home.HomeViewModel
import com.example.trustgate.presentation.features.home.HomeViewModelFactory
import com.example.trustgate.presentation.features.success.ui.SuccessScreen
import com.example.trustgate.presentation.features.auth.login.ui.LoginScreen
import com.example.trustgate.presentation.features.auth.signup.ui.SignupScreen
import com.example.trustgate.presentation.features.splash.SplashRoute
import com.example.trustgate.presentation.features.verification.VerificationViewModel
import com.example.trustgate.presentation.features.verification.VerificationViewModelFactory
import com.example.trustgate.presentation.features.verification.ui.VerificationShell
import com.example.trustgate.presentation.features.verification.ui.consent.VerificationConsentScreen
import com.example.trustgate.presentation.features.verification.ui.photo.VerificationPhotoScreen
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
) {
    val authRepo = remember { RepositoryProvider.provideAuthRepository() }
    val onboardingInfo = remember { RepositoryProvider.provideOnboardingDataStore() }
    val verificationRepo = remember { RepositoryProvider.provideVerificationRepository() }
    val authVm: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(
            authRepo,
            onboardingInfo
        )
    )
    val verificationVm: VerificationViewModel = viewModel(
        factory = VerificationViewModelFactory(
            verificationRepo
        )
    )

    NavHost(
        navController = navController,
        startDestination = AppScreens.Splash.route
    ) {
        composable(AppScreens.Splash.route) {
            SplashRoute(
                authViewModel = authVm,
                verificationViewModel = verificationVm,
                onGoToHome = {
                    navController.navigate(AppScreens.Home.route) {
                        popUpTo(AppScreens.Splash.route) { inclusive = true }
                    }
                },
                onGoToLogin = {
                    navController.navigate(AppScreens.Login.route) {
                        popUpTo(AppScreens.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(AppScreens.Login.route) { backStackEntry ->
            val scope = rememberCoroutineScope()

            LoginScreen(
                viewModel = authVm,
                onLoginSuccess = {
                    scope.launch{
                        val status = verificationVm.refreshAndGetStatus()
                        when (status) {
                            VerificationStatus.Completed -> {
                                navController.navigate(AppScreens.Home.route) {
                                    popUpTo(AppScreens.Login.route) { inclusive = true }
                                }
                            }
                            VerificationStatus.NotStarted -> {
                                navController.navigate(AppScreens.Consent.route)
                            }
                            VerificationStatus.ConsentGiven -> {
                                navController.navigate(AppScreens.Photo.route)
                            }
                        }
                    }
                },
                onSignupClick = {
                    navController.navigate(AppScreens.Signup.route)
                }
            )
        }

        composable(AppScreens.Signup.route) { backStackEntry ->
            SignupScreen(
                viewModel = authVm,
                onSignupSuccess = {
                    navController.navigate(AppScreens.Verification.route) {
                        popUpTo(AppScreens.Login.route) { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.popBackStack()
                }
            )
        }

        navigation(
            route = AppScreens.Verification.route,
            startDestination = AppScreens.Consent.route
        ) {
            composable(AppScreens.Consent.route) {
                VerificationShell(
                    viewModel = verificationVm,
                ) {
                    VerificationConsentScreen(
                        viewModel = verificationVm,
                        onContinueClick = {
                            if (verificationVm.state.value.consent) {
                                navController.navigate(AppScreens.Photo.route) {
                                    popUpTo(AppScreens.Consent.route) { inclusive = true }
                                }
                            }
                        }
                    )
                }
            }

            composable(AppScreens.Photo.route) {
                VerificationShell(
                    viewModel = verificationVm,
                ) {
                    VerificationPhotoScreen(
                        viewModel = verificationVm,
                        onContinueClick = {
                            verificationVm.restartState()
                            navController.navigate(AppScreens.Home.route) {
                                popUpTo(AppScreens.Photo.route) { inclusive = true }
                            }
                        }
                    )
                }
            }
        }

        composable(AppScreens.Home.route) {
            val gateRepo = remember { GateRepositoryImpl(GateSimulatedDataSource()) }
            val vm: HomeViewModel = viewModel(factory = HomeViewModelFactory(gateRepo))

            HomeScreen(
                viewModel = vm,
                onLogoutClick = {
                    authVm.logout()
                    navController.navigate(AppScreens.Login.route) {
                        popUpTo(AppScreens.Home.route) { inclusive = true }
                    }
                },
                onSuccess = { gateName ->
                    navController.navigate(AppScreens.Success.route)
                }
            )
        }

        composable(AppScreens.Success.route) {
            SuccessScreen(onGoHome = {
                navController.navigate(AppScreens.Home.route){
                    popUpTo(AppScreens.Success.route) { inclusive = true }
                    launchSingleTop = true
                    }
                }
            )
        }
    }
}