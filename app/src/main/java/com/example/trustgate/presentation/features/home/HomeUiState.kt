package com.example.trustgate.presentation.features.home

data class HomeUiState(
    val error: String? = null,
    val isSendingId: Boolean = false,
    val lastSentGateUid: String? = null
)