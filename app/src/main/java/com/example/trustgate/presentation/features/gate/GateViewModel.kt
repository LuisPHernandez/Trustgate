package com.example.trustgate.presentation.features.gate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trustgate.domain.repo.GateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GateViewModel(
    private val gateRepository: GateRepository,
    private val gateUid: String
): ViewModel() {
    // Estado de la Gate
    private val _ui = MutableStateFlow(GateUiState())
    val ui: StateFlow<GateUiState> = _ui

    init {
        observeEntries()
    }

    private fun observeEntries() {
        viewModelScope.launch {
            gateRepository.observeGateEntries(gateUid)
                .onStart {
                    _ui.update { it.copy(isLoading = true, error = null) }
                }
                .catch { e ->
                    _ui.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Error cargando historial de ingresos"
                        )
                    }
                }
                .collect { entries ->
                    _ui.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                            entries = entries
                        )
                    }
                }
        }
    }
}