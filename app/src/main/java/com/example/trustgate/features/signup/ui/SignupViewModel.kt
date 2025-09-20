package com.example.trustgate.features.signup.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class SignupViewModel: ViewModel() {
    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    fun onNameChange(new: String) { name = new }
    fun onEmailChange(new: String) { email = new }
    fun onPasswordChange(new: String) { password = new }
}