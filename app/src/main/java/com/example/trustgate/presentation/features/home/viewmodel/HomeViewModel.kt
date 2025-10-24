package com.example.trustgate.presentation.features.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trustgate.domain.model.GateScanResult
import com.example.trustgate.domain.repo.GateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeState(
    val isScanning: Boolean = false,
    val error: String? = null,
    val lastResult: GateScanResult? = null
)

class HomeViewModel(
    private val gate: GateRepository
) : ViewModel() {
    private val _ui = MutableStateFlow(HomeState())
    val ui: StateFlow<HomeState> = _ui

    fun scan() {
        viewModelScope.launch {
            gate.scanGate()
                .onStart {
                    _ui.update { it.copy(isScanning = true, error = null, lastResult = null) }
                }
                .catch { e ->
                    _ui.update { it.copy(error = e.message ?: "El scan falló") }
                }
                .collect { result ->
                    _ui.update { it.copy(lastResult = result) }
                }
            _ui.update { it.copy(isScanning = false) }
        }
    }

    // Limpiar resultados y errores después de mostrarlos/usarlos
    fun clear() {
        _ui.update { it.copy(lastResult = null, error = null) }
    }
}