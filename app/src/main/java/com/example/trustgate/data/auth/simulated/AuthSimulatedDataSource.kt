package com.example.trustgate.data.auth.simulated

import com.example.trustgate.domain.datasource.AuthDataSource
import com.example.trustgate.domain.model.Session
import com.example.trustgate.domain.model.User
import com.example.trustgate.domain.model.UserRole
import kotlinx.coroutines.delay
import java.util.UUID
import kotlin.random.Random

// Fuente de datos simulada para la autenticación
class AuthSimulatedDataSource(
    private val latencyMs: Long = 1200L,     // Para simular la latencia de red
    private val failRate: Float = 0.50f      // Para simular errores de red (50% de probabilidad)
): AuthDataSource {
    data class UserRecord(
        val user: User,
        val passwordHash: String
    )

    // "Data base" falsa
    private val usersByEmail = mutableMapOf<String, UserRecord>()
    private var currentSession: Session? = null

    // Crea un usuario y devuelve sesión activa, falla si ya existe el email
    override suspend fun signup(name: String, email: String, password: String): Session {
        simulateLatencyOrThrow()
        check(!usersByEmail.containsKey(email)) { "El email ya está registrado" }

        val user = User(
            id = "u-${UUID.randomUUID()}",
            name = name,
            email = email
        )
        val record = UserRecord(user, password.hash())
        usersByEmail[email] = record

        return startSessionFor(user)
    }

    // Valida usuario y devuelve sesión activa
    override suspend fun login(email: String, password: String): Session {
        simulateLatencyOrThrow()
        val record = usersByEmail[email] ?: error("Usuario no existe")
        check(record.passwordHash == password.hash()) { "Credenciales inválidas" }
        return startSessionFor(record.user)
    }

    // Cierra sesión activa
    override suspend fun logout() {
        delay(300)
        currentSession = null
    }

    // Devuelve la sesión vigente
    override suspend fun currentSession(): Session? {
        delay(100)
        return currentSession
    }

    // HELPERS
    private suspend fun simulateLatencyOrThrow() {
        delay(latencyMs)
        if (Random.nextFloat() < failRate) {
            throw RuntimeException("Fallo de conexión")
        }
    }

    private fun startSessionFor(user: User): Session {
        val token = "token-${UUID.randomUUID()}"
        val session = Session(token = token, user = user, role = UserRole.VISITOR)
        currentSession = session
        return session
    }

    private fun String.hash(): String = "h$length:${reversed()}"
}