package com.example.trustgate.presentation.features.login.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.trustgate.domain.model.Session
import com.example.trustgate.domain.repo.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val auth: AuthRepository
): ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var session by mutableStateOf<Session?>(null)

    fun onEmailChange(new: String) { email = new; error = null }

    fun onPasswordChange(new: String) { password = new; error = null }

    fun submit() {
        viewModelScope.launch {
            try {
                isLoading = true
                error = null
                val result = auth.login(email, password)
                session = result
            } catch (e: Exception) {
                error = e.message ?: "Error desconocido"
            } finally {
                isLoading = false
            }
        }
    }
}