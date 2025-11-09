package com.example.trustgate.core.common

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException

object AuthErrorMapper {
    fun map(e: Exception): String = when (e) {
        is FirebaseAuthInvalidCredentialsException -> {
            when (e.errorCode) {
                "ERROR_INVALID_EMAIL" -> "El correo electrónico no es válido."
                "ERROR_WRONG_PASSWORD" -> "La contraseña es incorrecta."
                "ERROR_WEAK_PASSWORD" -> "El password debe tener, al menos, 6 caracteres."
                else -> "Credenciales incorrectas."
            }
        }
        is FirebaseAuthInvalidUserException -> {
            when (e.errorCode) {
                "ERROR_USER_NOT_FOUND" -> "No existe una cuenta con este correo."
                "ERROR_USER_DISABLED" -> "Esta cuenta ha sido deshabilitada."
                else -> "No se pudo iniciar sesión."
            }
        }
        is FirebaseNetworkException -> "No hay conexión a internet."
        is FirebaseAuthUserCollisionException -> "a existe una cuenta con este correo electrónico."
        else -> "Error al iniciar sesión."
    }
}