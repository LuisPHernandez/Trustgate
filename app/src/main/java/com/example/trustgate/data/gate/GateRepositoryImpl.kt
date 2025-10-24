package com.example.trustgate.data.gate

import com.example.trustgate.data.gate.simulated.GateSimulatedDataSource
import com.example.trustgate.domain.repo.GateRepository
import kotlinx.coroutines.flow.Flow
import com.example.trustgate.domain.model.GateScanResult


// Implementación del repositorio de escaneo de portón
class GateRepositoryImpl(
    private val source: GateSimulatedDataSource
) : GateRepository {

    override fun scanGate(): Flow<GateScanResult> {
        return source.scanGate()
    }
}