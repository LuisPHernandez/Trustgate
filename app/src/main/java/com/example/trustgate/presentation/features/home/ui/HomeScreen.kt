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
import com.example.trustgate.domain.model.GateScanResult
import com.example.trustgate.presentation.features.home.HomeViewModel
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

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


    //libreria para QR Quickie
    val scanQRCodeLauncher =
        rememberLauncherForActivityResult(ScanQRCode()) { result: QRResult ->
            when (result) {
                is QRResult.QRSuccess -> {
                    val raw = result.content.rawValue.orEmpty()
                    val gateName = raw
                    onSuccess(gateName)
                    viewModel.clear()
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
    // Navegar cuando hay scan exitoso
    LaunchedEffect(state.lastResult) {
        when (state.lastResult) {
            is GateScanResult.Success -> {
                onSuccess((state.lastResult as GateScanResult.Success).gate.name)
                viewModel.clear()
            }
            GateScanResult.Denied -> {
                snackbarHostState.showSnackbar("Acceso denegado")
                viewModel.clear()
            }
            null -> Unit
        }
    }

    // Mostrar error de flujo
    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clear()
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
