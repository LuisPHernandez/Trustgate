package com.example.trustgate.presentation.features.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trustgate.domain.model.Session
import com.example.trustgate.domain.repo.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val session: Session? = null
)

class LoginViewModel(
    private val auth: AuthRepository
): ViewModel() {
    private val _ui = MutableStateFlow(LoginState())
    val ui: StateFlow<LoginState> = _ui

    fun onEmailChange(new: String) { _ui.update { it.copy(email = new, error = null) } }
    fun onPasswordChange(new: String) { _ui.update { it.copy(password = new, error = null) } }

    fun submit() {
        viewModelScope.launch {
            _ui.update { it.copy(isLoading = true, error = null) }
            try {
                val result = auth.login(_ui.value.email, _ui.value.password)
                _ui.update { it.copy(session = result) }
            } catch (e: Exception) {
                _ui.update { it.copy(error = e.message ?: "Error") }
            } finally {
                _ui.update { it.copy(isLoading = false) }
            }
        }
    }
}