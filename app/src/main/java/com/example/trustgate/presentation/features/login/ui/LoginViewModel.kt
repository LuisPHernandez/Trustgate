package com.example.trustgate.presentation.features.login.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class LoginViewModel: ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    fun onEmailChange(new: String) { email = new }
    fun onPasswordChange(new: String) { password = new }
}