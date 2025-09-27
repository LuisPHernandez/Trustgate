package com.example.trustgate.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.trustgate.features.home.ui.HomeScreen
import com.example.trustgate.features.home.ui.SuccessScreen
import com.example.trustgate.features.login.ui.LoginScreen
import com.example.trustgate.features.signup.ui.SignupScreen
import com.example.trustgate.features.verification.navigation.VerificationNavigation

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = AppScreens.Login.route
    ) {
        composable(AppScreens.Login.route) {
            LoginScreen(
                onLoginClick = {
                    navController.navigate(AppScreens.Home.route) {
                        popUpTo(AppScreens.Login.route) { inclusive = true }
                    }
                },
                onSignupClick = {
                    navController.navigate(AppScreens.Signup.route)
                }
            )
        }

        composable(AppScreens.Signup.route) {
            SignupScreen(
                onSignupClick = {
                    navController.navigate(AppScreens.Verification.route) {
                        popUpTo(AppScreens.Login.route) { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppScreens.Verification.route) {
            VerificationNavigation(
                onFinish = { uri ->
                    navController.navigate(AppScreens.Home.route) {
                        popUpTo(AppScreens.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(AppScreens.Home.route) {
            HomeScreen(
                onLogoutClick = {
                    navController.navigate(AppScreens.Login.route) {
                        popUpTo(AppScreens.Home.route) { inclusive = true }
                    }
                },
                onScanClick = {
                    navController.navigate(AppScreens.Success.route)
                }
            )
        }

        composable(AppScreens.Success.route) {
            SuccessScreen()
        }
    }
}