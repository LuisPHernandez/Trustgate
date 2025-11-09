package com.example.trustgate.domain.repo

import com.example.trustgate.domain.model.VerificationStatus

interface VerificationRepository {
    suspend fun giveConsent(): VerificationStatus
    suspend fun uploadIdPhoto(uid: String, bytes: ByteArray): VerificationStatus
    suspend fun status(): VerificationStatus
}