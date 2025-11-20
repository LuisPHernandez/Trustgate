package com.example.trustgate.presentation.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trustgate.domain.repo.GateRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val gate: GateRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private val _ui = MutableStateFlow(HomeUiState())
    val ui: StateFlow<HomeUiState> = _ui

    fun sendIdToGate(gateUid: String) {
        viewModelScope.launch(ioDispatcher) {
            _ui.update {
                it.copy(
                    isSendingId = true,
                    error = null,
                    lastSentGateUid = null
                )
            }
            try {
                gate.sendVisitorIdToGate(gateUid)
                _ui.update {
                    it.copy(
                        isSendingId = false,
                        lastSentGateUid = gateUid
                    )
                }
            } catch (e: Exception) {
                _ui.update {
                    it.copy(
                        isSendingId = false,
                        error = e.message ?: "No se pudo enviar el documento de identificación"
                    )
                }
            }
        }
    }

    // Limpiar resultados y errores después de mostrarlos/usarlos
    fun clear() {
        _ui.update { it.copy(error = null) }
    }

    // Limpiar el flag de éxito
    fun clearLastSentGateUid() {
        _ui.update { it.copy(lastSentGateUid = null) }
    }
}