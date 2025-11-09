package com.example.trustgate.presentation.features.home

import com.example.trustgate.domain.model.GateScanResult

data class HomeUiState(
    val isScanning: Boolean = false,
    val error: String? = null,
    val lastResult: GateScanResult? = null
)