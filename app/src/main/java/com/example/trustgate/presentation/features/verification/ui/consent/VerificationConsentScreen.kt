package com.example.trustgate.presentation.features.verification.ui.consent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.trustgate.core.ui.components.BulletedRow
import com.example.trustgate.core.ui.components.ContinueButton
import com.example.trustgate.core.ui.components.TitleText
import com.example.trustgate.presentation.features.verification.VerificationViewModel

@Composable
fun VerificationConsentScreen(
    viewModel: VerificationViewModel,
    onContinueClick: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        TitleText(
            text = "Comienza tu verificación"
        )

        Text(
            text = "Este proceso está diseñado para verificar tu identidad de manera " +
                    "segura. Se usa tu documento de identificación para compartir tu " +
                    "información personal con un guardia de seguridad de manera rápida " +
                    "y automática.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                BulletedRow(text = "Usa una licencia de conducrir válida, emitida por" +
                        " el gobierno")

                BulletedRow(text = "Busca una superficie bien iluminada")
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = state.consent,
                onCheckedChange = { viewModel.onConsentChange() }
            )

            Text(
                text = "Doy consentimiento a Trustgate de recopilar, procesar y " +
                        "compartir mi información personal.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        ContinueButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            label = if (state.isLoading) "Cargando..." else "Continuar",
            labelStyle = MaterialTheme.typography.labelSmall,
            labelColor = MaterialTheme.colorScheme.onPrimary,
            onClick = onContinueClick,
            enabled = state.consent
        )
    }
}