package com.example.trustgate.domain.repo

import com.example.trustgate.domain.model.VerificationStatus

interface VerificationRepository {
    suspend fun giveConsent(): VerificationStatus
    suspend fun uploadIdPhoto(localUri: String): VerificationStatus
    suspend fun status(): VerificationStatus
}