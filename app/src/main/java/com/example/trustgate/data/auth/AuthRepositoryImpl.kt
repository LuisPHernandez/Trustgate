package com.example.trustgate.data.auth

import com.example.trustgate.domain.model.Session
import com.example.trustgate.domain.repo.AuthRepository
import com.example.trustgate.domain.datasource.AuthDataSource

// Implementación del repositorio de autenticación
class AuthRepositoryImpl(
    private val source: AuthDataSource
): AuthRepository {

    override suspend fun signup(name: String, email: String, password: String): Session {
        return source.signup(name, email, password)
    }

    override suspend fun login(email: String, password: String): Session {
        return source.login(email, password)
    }

    override suspend fun logout() {
        return source.logout()
    }

    override suspend fun currentSession(): Session? {
        return source.currentSession()
    }
}