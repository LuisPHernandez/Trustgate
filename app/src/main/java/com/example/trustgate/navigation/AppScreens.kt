package com.example.trustgate.navigation

sealed class AppScreens(val route: String) {
    object Splash: AppScreens("splash")
    object Login: AppScreens("login")
    object Signup: AppScreens("signup")
    object Verification: AppScreens("verification")
    object Consent: AppScreens("verification/consent")
    object Photo: AppScreens("verification/photo")
    object Home: AppScreens("home")
    object Success : AppScreens("success")
    object GateHome : AppScreens("gate_home")
}