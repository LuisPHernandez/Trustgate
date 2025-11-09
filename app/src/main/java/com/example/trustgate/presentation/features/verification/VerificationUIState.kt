package com.example.trustgate.presentation.features.verification

import android.graphics.Bitmap
import com.example.trustgate.domain.model.VerificationStatus

data class VerificationUiState(
    val isUploading: Boolean = false,
    val isLoading: Boolean = false,
    val lastPhoto: Bitmap? = null,
    val error: String? = null,
    val consent: Boolean = false,
    val status: VerificationStatus = VerificationStatus.NotStarted
)