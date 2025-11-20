package com.example.trustgate.presentation.features.home.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.trustgate.core.ui.components.ContinueButton
import com.example.trustgate.presentation.features.home.HomeViewModel
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode
import kotlinx.coroutines.launch
import android.net.Uri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onLogoutClick: () -> Unit,
    onSuccess: (gateName: String) -> Unit,
) {
    val state by viewModel.ui.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()


    // Libreria para QR Quickie
    val scanQRCodeLauncher =
        rememberLauncherForActivityResult(ScanQRCode()) { result: QRResult ->
            when (result) {
                is QRResult.QRSuccess -> {
                    val raw = result.content.rawValue.orEmpty()
                    val gateUid = parseGateUidFromQr(raw)
                    if (gateUid != null) {
                        viewModel.sendIdToGate(gateUid)
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("QR no válido")
                        }
                    }
                }


                QRResult.QRUserCanceled -> Unit
                is QRResult.QRError -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Error al escanear el QR")
                    }
                }

                QRResult.QRMissingPermission -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Permisos de cámara denegados")
                    }
                }
            }
        }

    // Mostrar error de flujo
    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clear()
        }
    }

    // Navegar cuando el envío al gate fue exitoso
    LaunchedEffect(state.lastSentGateUid) {
        state.lastSentGateUid?.let { gateUid ->
            onSuccess(gateUid)
            viewModel.clearLastSentGateUid()
        }
    }

    val applicationContext = LocalContext.current
    val controller = remember {
        LifecycleCameraController(applicationContext).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Bienvenido a Trustgate",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onLogoutClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Logout"
                        )
                    }
                }
            )
        },
    )
    { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(25.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.medium
            ) {
                HomeCameraPreview(
                    controller = controller,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                ContinueButton(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(55.dp),
                    label = "Escanear",
                    labelStyle = MaterialTheme.typography.labelSmall,
                    labelColor = MaterialTheme.colorScheme.onPrimary,
                    onClick = { scanQRCodeLauncher.launch(null) },
                    enabled = true
                )
            }
        }
    }
}

private fun parseGateUidFromQr(raw: String): String? {
    return try {
        val uri = Uri.parse(raw)
        if (uri.scheme == "trustgate" && uri.host == "gate") {
            uri.getQueryParameter("uid")
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }
}