package com.example.trustgate.data.verification.simulated

import com.example.trustgate.domain.model.VerificationStatus
import kotlinx.coroutines.delay
import kotlin.random.Random

// Fuente de datos simulada para la verificación de identidad
class VerificationSimulatedDataSource(
    private val latencyMs: Long = 1200L,    // Para simular la latencia de red
    private val failRate: Float = 0.1f      // Para simular errores de red (10% de probabilidad)
) {
    private var status: VerificationStatus = VerificationStatus.NotStarted

    // Marca que el usuario dio su consentimiento para la verificación
    suspend fun giveConsent(): VerificationStatus {
        simulateLatencyOrThrow()
        status = VerificationStatus.ConsentGiven
        return status
    }

    // Simula subir la foto del documento de identidad
    suspend fun uploadIdPhoto(localUri: String): VerificationStatus {
        simulateLatencyOrThrow()
        // Se simula éxito o fallo de reconocimiento de imagen
        if (Random.nextFloat() < (failRate / 2)) {
            throw RuntimeException("Error al procesar la foto del documento")
        }
        status = VerificationStatus.PhotoCaptured
        delay(500)
        status = VerificationStatus.Completed
        return status
    }

    // Devuelve el estado actual (guardado en memoria)
    suspend fun currentStatus(): VerificationStatus {
        delay(200)
        return status
    }

    // HELPER
    private suspend fun simulateLatencyOrThrow() {
        delay(latencyMs)
        if (Random.nextFloat() < failRate) {
            throw RuntimeException("Fallo de conexión")
        }
    }
}