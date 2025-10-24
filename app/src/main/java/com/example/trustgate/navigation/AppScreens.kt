package com.example.trustgate.navigation

sealed class AppScreens(val route: String) {
    object Login: AppScreens("login")
    object Signup: AppScreens("signup")
    object Verification: AppScreens("verification")
    object Home: AppScreens("home")
    object Success : AppScreens("success")
}