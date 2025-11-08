package com.example.trustgate.presentation.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

//definir cuales son los posibles estados de la UI durante la autentificacion


sealed class AuthUiState{
    data object Idle: AuthUiState() // estado inicial
    data object Loading: AuthUiState()
    data object Success: AuthUiState()
    data class Error(val message: String): AuthUiState()
}

class AuthViewModel : ViewModel(){
    private val auth: FirebaseAuth = Firebase.auth

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun register(email: String, password: String){
        viewModelScope.launch{
            _uiState.value = AuthUiState.Loading
            try{
                //usamos la funcion de firebase para registrar un nuevo usuario
                auth.createUserWithEmailAndPassword(email, password).await()
                //actualizamos el estado a exitoso
                _uiState.value = AuthUiState.Success
            }catch (e: Exception){
                // si falla se cambia el estado a error y se da el mensaje
                _uiState.value = AuthUiState.Error(e.message ?: "Error al registrar")
            }
        }
    }

    fun login(email: String, password: String){
        viewModelScope.launch{
            _uiState.value = AuthUiState.Loading
            try{
                auth.signInWithEmailAndPassword(email, password).await()
                _uiState.value = AuthUiState.Success
            }catch (e: Exception){
                _uiState.value = AuthUiState.Error(e.message ?: "Error al iniciar sesion")
            }
        }
    }
    //se reinicia el estado, despues de mostrar errores o cuando se necesite
    fun resetState(){
        _uiState.value = AuthUiState.Idle
    }

}
