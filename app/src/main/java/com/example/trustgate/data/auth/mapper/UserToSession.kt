package com.example.trustgate.data.auth.mapper

import com.example.trustgate.domain.model.Session
import com.example.trustgate.domain.model.User
import com.example.trustgate.domain.model.UserRole
import com.google.firebase.auth.FirebaseUser

// Convierte user a session
fun FirebaseUser.toSession(
    token: String,
    fallbackName: String? = null,
    role: UserRole
): Session {
    val resolvedName = when {
        !this.displayName.isNullOrBlank() -> this.displayName!!
        !fallbackName.isNullOrBlank() -> fallbackName
        !this.email.isNullOrBlank() -> this.email!!.substringBefore("@")
        else -> "User"
    }
    val resolvedEmail = this.email ?: ""
    val u = User(id = this.uid, name = resolvedName, email = resolvedEmail)
    return Session(token = token, user = u, role = role)
}