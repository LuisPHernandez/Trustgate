package com.example.trustgate.presentation.features.verification.navigation

sealed class VerificationInnerScreens(val route: String) {
    object Consent : VerificationInnerScreens("consent")
    object Photo   : VerificationInnerScreens("photo")
}