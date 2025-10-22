package com.example.trustgate.data.verification

import com.example.trustgate.data.verification.simulated.VerificationSimulatedDataSource
import com.example.trustgate.domain.model.VerificationStatus
import com.example.trustgate.domain.repo.VerificationRepository

// Implementación del repositorio de verificación de identidad
class VerificationRepositoryImpl(
    private val source: VerificationSimulatedDataSource
) : VerificationRepository {

    override suspend fun giveConsent(): VerificationStatus {
        return source.giveConsent()
    }

    override suspend fun uploadIdPhoto(localUri: String): VerificationStatus {
        return source.uploadIdPhoto(localUri)
    }

    override suspend fun status(): VerificationStatus {
        return source.currentStatus()
    }
}