package com.example.trustgate.domain.repo

import com.example.trustgate.domain.model.Session

interface AuthRepository {
    suspend fun signup(name: String, email: String, password: String): Session
    suspend fun login(email: String, password: String): Session
    suspend fun logout()
    suspend fun currentSession(): Session?
}