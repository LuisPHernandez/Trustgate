package com.example.trustgate.presentation.features.verification.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class VerificationViewModel: ViewModel() {
    var consent by mutableStateOf(false)

    fun onConsentChange(new: Boolean) { consent = new }
}