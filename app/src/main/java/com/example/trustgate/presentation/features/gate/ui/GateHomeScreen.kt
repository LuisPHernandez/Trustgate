package com.example.trustgate.presentation.features.gate.ui

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.trustgate.presentation.features.gate.GateViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GateHomeScreen(
    uid: String,
    onLogoutClick: () -> Unit,
    viewModel: GateViewModel
) {
    // Contenido que irá dentro del QR.
    val qrContent = remember(uid) { "trustgate://gate?uid=$uid" }
    val qrBitmap = remember(qrContent) { generateQrBitmap(qrContent) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Pantalla de control",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onLogoutClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Cerrar sesión"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Mostrar QR a visitantes para que lo escaneen con Trustgate.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            if (qrBitmap != null) {
                Image(
                    bitmap = qrBitmap.asImageBitmap(),
                    contentDescription = "Código QR de la gate",
                    modifier = Modifier.size(260.dp)
                )
            } else {
                Text(
                    text = "No se pudo generar el código QR.",
                    color = MaterialTheme.colorScheme.error
                )
            }

            Text(
                text = "UID: $uid",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )

            // Sección de entradas recientes
            GateEntriesSection(
                viewModel = viewModel,
                modifier = Modifier.weight(1f, fill = true)
            )
        }
    }
}

private fun generateQrBitmap(content: String, size: Int = 512): Bitmap? {
    return try {
        val bitMatrix: BitMatrix =
            MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size)
        val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
        for (x in 0 until size) {
            for (y in 0 until size) {
                bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }
        bmp
    } catch (e: Exception) {
        null
    }
}