package com.example.trustgate.presentation.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trustgate.data.local.OnboardingDataStore
import com.example.trustgate.domain.repo.AuthRepository

class AuthViewModelFactory(
    private val repo: AuthRepository,
    private val onboardingInfo: OnboardingDataStore
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(repo, onboardingInfo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}