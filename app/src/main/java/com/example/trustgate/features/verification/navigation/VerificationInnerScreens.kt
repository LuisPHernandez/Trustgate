package com.example.trustgate.features.verification.navigation

sealed class VerificationInnerScreens(val route: String) {
    object Consent : VerificationInnerScreens("consent")
    object Photo   : VerificationInnerScreens("photo")
}