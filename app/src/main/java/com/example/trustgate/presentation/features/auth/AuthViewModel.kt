package com.example.trustgate.presentation.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trustgate.core.common.AuthErrorMapper
import com.example.trustgate.data.local.OnboardingDataStore
import com.example.trustgate.domain.repo.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repo: AuthRepository,
    val onboardingInfo: OnboardingDataStore
): ViewModel(){
    // Estado actual de la autenticación
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    val keepSignedIn = onboardingInfo.keepSignedIn

    fun signup(name: String, email: String, password: String){
        viewModelScope.launch{
            _uiState.value = AuthUiState.Loading
            try{
                repo.signup(name, email, password)

                // Si el signup tiene éxito, actualizamos el estado a Success
                _uiState.value = AuthUiState.Success
            } catch (e: Exception){
                // Si el signup falla, se cambia el estado a Error y se da el mensaje
                _uiState.value = AuthUiState.Error(AuthErrorMapper.map(e))
            }
        }
    }

    fun login(email: String, password: String){
        viewModelScope.launch{
            _uiState.value = AuthUiState.Loading
            try{
                repo.login(email, password)

                // Guardamos el email en DataStore para agilizar login la próxima vez
                onboardingInfo.setLastLoggedInEmail(email)

                // Si el login tiene éxito, actualizamos el estado a Success
                _uiState.value = AuthUiState.Success
            } catch (e: Exception){
                // Si el login falla, se cambia el estado a Error y se da el mensaje
                _uiState.value = AuthUiState.Error(AuthErrorMapper.map(e))
            }
        }
    }

    fun setKeepSignedIn(value: Boolean) {
        viewModelScope.launch {
            onboardingInfo.setKeepSignedIn(value)
        }
    }

    fun logout() {
        viewModelScope.launch {
            repo.logout()
        }
    }

    // Se reinicia el estado de autenticación
    fun resetState(){
        _uiState.value = AuthUiState.Idle
    }

}
