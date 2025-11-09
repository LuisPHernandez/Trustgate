package com.example.trustgate.data.verification

import com.example.trustgate.data.verification.remote.VerificationRemoteDataSource
import com.example.trustgate.domain.model.VerificationStatus
import com.example.trustgate.domain.repo.VerificationRepository
import com.google.firebase.auth.FirebaseAuth

// Implementación del repositorio de verificación de identidad
class VerificationRepositoryImpl(
    private val source: VerificationRemoteDataSource
): VerificationRepository {
    override suspend fun giveConsent(): VerificationStatus {
        val user = FirebaseAuth.getInstance().currentUser
        val uid = requireNotNull(user?.uid) { "No hay usuario autenticado" }
        source.setStatus(uid, VerificationStatus.ConsentGiven)
        return VerificationStatus.ConsentGiven
    }

    override suspend fun uploadIdPhoto(uid: String, bytes: ByteArray): VerificationStatus {
        source.uploadIdPhoto(uid, bytes)
        source.setStatus(uid, VerificationStatus.Completed)
        return VerificationStatus.Completed
    }

    override suspend fun status(): VerificationStatus {
        val user = FirebaseAuth.getInstance().currentUser
        val uid = requireNotNull(user?.uid) { "No hay usuario autenticado" }
        return source.getStatus(uid)
    }
}