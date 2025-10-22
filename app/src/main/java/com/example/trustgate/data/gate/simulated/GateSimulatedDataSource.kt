package com.example.trustgate.data.gate.simulated

import com.example.trustgate.domain.model.Gate
import com.example.trustgate.domain.model.GateScanResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

// Fuente de datos simulada para el escaneo del portón
class GateSimulatedDataSource(
    private val latencyMs: Long = 1500L,    // Para simular la latencia de red
    private val failRate: Float = 0.2f      // Para simular errores de red (20% de probabilidad)
) {
    fun scanGate(): Flow<GateScanResult> = flow {
        // Se simula la latencia
        delay(latencyMs)

        // Se simula un fallo de red aleatorio
        if (Random.nextFloat() < failRate) {
            throw RuntimeException("Error de conexión al servidor del portón")
        }

        // Decidimos aleatoriamente si el acceso fue concedido o negado
        val granted = Random.nextBoolean()
        if (granted) {
            val randomId = Random.nextInt(1, 2)
            val gate = Gate(
                id = "gate-${randomId}",
                name = if (randomId == 1) "Entrada Principal" else "Entrada Secundaria"
            )
            emit(GateScanResult.Success(gate))
        } else {
            emit(GateScanResult.Denied)
        }
    }
}