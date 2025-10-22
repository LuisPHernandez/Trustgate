package com.example.trustgate.presentation.features.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trustgate.domain.model.GateScanResult
import com.example.trustgate.domain.repo.GateRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HomeViewModel(
    private val gate: GateRepository
) : ViewModel() {

    var isScanning by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var lastResult by mutableStateOf<GateScanResult?>(null)

    fun scan() {
        viewModelScope.launch {
            gate.scanGate()
                .onStart {
                    isScanning = true
                    error = null
                    lastResult = null
                }
                .catch { e ->
                    error = e.message ?: "Scan failed"
                }
                .collect { result ->
                    lastResult = result   // Success o Denied
                }
            isScanning = false
        }
    }

    // Limpiar resultados y errores después de mostrarlos/usarlos
    fun clear() {
        lastResult = null
        error = null
    }
}