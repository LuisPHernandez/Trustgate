package com.example.trustgate.domain.model

data class Session(
    val token: String,
    val user: User,
    val role: UserRole
)