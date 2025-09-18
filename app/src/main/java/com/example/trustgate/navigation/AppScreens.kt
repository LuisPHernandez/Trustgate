package com.example.trustgate.navigation

sealed class AppScreens(val route: String) {
    object Login: AppScreens("login")
    object SignUp: AppScreens("signup")
    object HomeScreen: AppScreens("home")
}