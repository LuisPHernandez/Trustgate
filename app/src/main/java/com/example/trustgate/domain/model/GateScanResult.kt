package com.example.trustgate.domain.model

sealed interface GateScanResult {
    data class Success(val gate: Gate) : GateScanResult
    data object Denied : GateScanResult
}