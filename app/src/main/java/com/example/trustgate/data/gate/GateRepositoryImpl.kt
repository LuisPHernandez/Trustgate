package com.example.trustgate.data.gate

import com.example.trustgate.domain.model.GateEntry
import com.example.trustgate.domain.repo.GateRepository
import kotlinx.coroutines.flow.Flow
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

// Implementación del repositorio de escaneo de portón
class GateRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val auth: FirebaseAuth
) : GateRepository {

    override suspend fun sendVisitorIdToGate(gateUid: String) {
        val currentUser = auth.currentUser
            ?: throw IllegalStateException("No hay usuario autenticado")

        val visitorUid = currentUser.uid

        // Referencia a la carpeta del usuario
        val folderRef = storage.reference.child("personal_documents/$visitorUid")

        // Lista todos los archivos en la carpeta
        val items = folderRef.listAll().await()

        if (items.items.isEmpty()) {
            throw IllegalStateException("No se encontró ninguna imagen de identificación.")
        }

        // Se obtiene el archivo más reciente
        val latestRef = items.items.maxByOrNull { item ->
            item.name.removeSuffix(".jpg").toLongOrNull() ?: 0L
        } ?: throw IllegalStateException("No se pudo determinar la última imagen.")

        // Se obtiene el URL temporal
        val downloadUrl = latestRef.downloadUrl.await().toString()

        val requestData = mapOf(
            "visitorUid" to visitorUid,
            "photoUrl" to downloadUrl,
            "timestamp" to FieldValue.serverTimestamp()
        )

        firestore.collection("gates")
            .document(gateUid)
            .collection("incoming_requests")
            .add(requestData)
            .await()
    }

    override fun observeGateEntries(gateUid: String): Flow<List<GateEntry>> = callbackFlow {
        val registration = firestore.collection("gates")
            .document(gateUid)
            .collection("incoming_requests")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                val docs = snapshot?.documents.orEmpty()

                val entries = docs.map { doc ->
                    val visitorUid = doc.getString("visitorUid").orEmpty()
                    val photoUrl = doc.getString("photoUrl").orEmpty()
                    val ts = doc.getTimestamp("timestamp")?.toDate()?.time

                    GateEntry(
                        id = doc.id,
                        visitorUid = visitorUid,
                        photoUrl = photoUrl,
                        timestampMillis = ts
                    )
                }

                trySend(entries)
            }

        awaitClose {
            registration.remove()
        }
    }

}