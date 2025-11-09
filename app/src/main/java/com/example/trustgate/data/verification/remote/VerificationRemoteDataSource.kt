package com.example.trustgate.data.verification.remote

import com.example.trustgate.domain.model.VerificationStatus
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await

class VerificationRemoteDataSource {
    private val storage = Firebase.storage
    private val db = Firebase.firestore

    private fun statusDoc(uid: String) =
        db.collection("verifications").document(uid)

    suspend fun setStatus(uid: String, status: VerificationStatus) {
        statusDoc(uid).set(
            mapOf(
                "status" to status.name,
                "updatedAt" to FieldValue.serverTimestamp()
            ),
            com.google.firebase.firestore.SetOptions.merge()
        ).await()
    }

    suspend fun getStatus(uid: String): VerificationStatus {
        val snap = statusDoc(uid).get().await()
        val raw = snap.getString("status") ?: VerificationStatus.NotStarted.name
        return runCatching { VerificationStatus.valueOf(raw) }
            .getOrDefault(VerificationStatus.NotStarted)
    }

    suspend fun uploadIdPhoto(uid: String, bytes: ByteArray) {
        val path = "personal_documents/$uid/${System.currentTimeMillis()}.jpg"
        val ref = storage.reference.child(path)

        val metadata = StorageMetadata.Builder()
            .setContentType("image/jpeg")
            .setCustomMetadata("ownerUid", uid)
            .build()

        ref.putBytes(bytes, metadata).await()
    }
}