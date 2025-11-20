package com.example.trustgate.presentation.features.gate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trustgate.domain.repo.GateRepository

class GateViewModelFactory(
    private val repository: GateRepository,
    private val gateUid: String
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GateViewModel::class.java)) {
            return GateViewModel(
                repository,
                gateUid
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
