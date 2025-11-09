package com.example.trustgate.data.auth.remote

import com.example.trustgate.data.auth.mapper.toSession
import com.example.trustgate.domain.datasource.AuthDataSource
import com.example.trustgate.domain.model.Session
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.tasks.await

class AuthRemoteDataSource(
    private val auth: FirebaseAuth = Firebase.auth
): AuthDataSource {
    // Crea un usuario y devuelve sesión activa
    override suspend fun signup(name: String, email: String, password: String): Session {
        // Crear el usuario con Firebase
        auth.createUserWithEmailAndPassword(email, password).await()

        val user = requireNotNull(auth.currentUser) { "No hay usuario luego de signup" }
        if (name.isNotBlank() && user.displayName.isNullOrBlank()) {
            val req = userProfileChangeRequest { displayName = name }
            user.updateProfile(req).await()
        }

        val token = user.getIdToken(false).await().token ?: ""
        return user.toSession(token = token, fallbackName = name)
    }

    // Valida usuario y devuelve sesión activa
    override suspend fun login(email: String, password: String): Session {
        auth.signInWithEmailAndPassword(email, password).await()
        val user = requireNotNull(auth.currentUser) { "No hay usuario luego de login" }
        val token = user.getIdToken(false).await().token ?: ""
        return user.toSession(token = token)
    }

    // Cierra sesión activa
    override suspend fun logout() {
        auth.signOut()
    }

    // Devuelve la sesión vigente
    override suspend fun currentSession(): Session? {
        val user = auth.currentUser ?: return null
        val token = user.getIdToken(false).await().token ?: ""
        return user.toSession(token = token)
    }
}