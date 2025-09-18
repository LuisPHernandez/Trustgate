package com.example.trustgate.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.trustgate.features.home.ui.HomeScreen
import com.example.trustgate.features.login.ui.LoginScreen
import com.example.trustgate.features.signup.ui.SignupScreen

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
                    navController.navigate(AppScreens.HomeScreen.route) {
                        popUpTo(AppScreens.Login.route) { inclusive = true }
                    }
                },
                onSignupClick = {
                    navController.navigate(AppScreens.SignUp.route)
                }
            )
        }

        composable(AppScreens.SignUp.route) {
            SignupScreen()
        }

        composable(AppScreens.HomeScreen.route) {
            HomeScreen()
        }
    }
}