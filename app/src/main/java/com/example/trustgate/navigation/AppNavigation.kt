package com.example.trustgate.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.trustgate.features.home.ui.HomeScreen
import com.example.trustgate.features.login.ui.LoginScreen
import com.example.trustgate.features.signup.ui.SignupScreen
import com.example.trustgate.features.verification.ui.VerificationScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
) {
    var consent by rememberSaveable { mutableStateOf(false) }

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
            VerificationScreen(
                checked = consent,
                onCheckedChange = { consent = it },
                onContinueClick = {
                    if (consent) {
                        navController.navigate(AppScreens.Home.route) {
                            popUpTo(AppScreens.Verification.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(AppScreens.Home.route) {
            HomeScreen()
        }
    }
}