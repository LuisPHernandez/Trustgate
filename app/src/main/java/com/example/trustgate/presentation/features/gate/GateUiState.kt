package com.example.trustgate.presentation.features.gate

import com.example.trustgate.domain.model.GateEntry

data class GateUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val entries: List<GateEntry> = emptyList()
)