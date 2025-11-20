package com.example.trustgate.presentation.features.gate.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.trustgate.presentation.features.gate.GateViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun GateEntriesSection(
    viewModel: GateViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.ui.collectAsStateWithLifecycle()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Historial de ingresos",
            style = MaterialTheme.typography.titleMedium
        )

        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (state.entries.isEmpty()) {
            Text(
                text = "Todavía no hay ingresos registrados.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.entries, key = { it.id }) { entry ->
                    GateEntryItem(
                        visitorUid = entry.visitorUid,
                        photoUrl = entry.photoUrl,
                        timestampMillis = entry.timestampMillis
                    )
                }
            }
        }
    }
}

@Composable
private fun GateEntryItem(
    visitorUid: String,
    photoUrl: String,
    timestampMillis: Long?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = photoUrl,
                contentDescription = "Documento de $visitorUid",
                modifier = Modifier
                    .size(64.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "UID visitante: $visitorUid",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (timestampMillis != null) {
                    val formatted = formatTimestamp(timestampMillis)
                    Text(
                        text = "Entrada: $formatted",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

private fun formatTimestamp(millis: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(millis))
}
