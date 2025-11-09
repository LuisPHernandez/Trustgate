package com.example.trustgate.domain.datasource

import com.example.trustgate.domain.model.Session

interface AuthDataSource {
    suspend fun signup(name: String, email: String, password: String): Session
    suspend fun login(email: String, password: String): Session
    suspend fun logout()
    suspend fun currentSession(): Session?
}