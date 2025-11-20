package com.example.trustgate.domain.repo

import com.example.trustgate.domain.model.GateEntry
import kotlinx.coroutines.flow.Flow

interface GateRepository {
    // Mandar documento de identificación a la Gate
    suspend fun sendVisitorIdToGate(gateUid: String)

    // Observar las entradas a una Gate
    fun observeGateEntries(gateUid: String): Flow<List<GateEntry>>
}