package com.example.trustgate.domain.repo

import com.example.trustgate.domain.model.GateScanResult
import kotlinx.coroutines.flow.Flow

interface GateRepository {
    fun scanGate(): Flow<GateScanResult>
}