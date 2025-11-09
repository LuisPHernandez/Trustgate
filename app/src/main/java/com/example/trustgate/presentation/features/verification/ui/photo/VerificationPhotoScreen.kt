package com.example.trustgate.presentation.features.verification.ui.photo

import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.trustgate.core.ui.components.ContinueButton
import com.example.trustgate.domain.model.VerificationStatus
import com.example.trustgate.presentation.features.verification.VerificationViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun VerificationPhotoScreen(
    viewModel: VerificationViewModel,
    onContinueClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val applicationContext = LocalContext.current
    val controller = remember {
        LifecycleCameraController(applicationContext).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE
            )
        }
    }

    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    if (!cameraPermissionState.status.isGranted) {
        PermissionRationale(cameraPermissionState)
        return
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.7f)
                .padding(20.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = MaterialTheme.shapes.medium
        ) {
            if (state.lastPhoto == null) {
                // Mostrar cámara
                VerificationPhotoCameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    controller = controller
                )
            } else {
                // Mostrar previsualización
                Image(
                    bitmap = state.lastPhoto!!.asImageBitmap(),
                    contentDescription = "Previsualización del documento",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (state.lastPhoto == null) {
                ContinueButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(55.dp),
                    label = if (state.isLoading) "Verificando..." else "Tomar Foto",
                    labelStyle = MaterialTheme.typography.labelSmall,
                    labelColor = MaterialTheme.colorScheme.onPrimary,
                    onClick = {
                        viewModel.takePhoto(
                            controller = controller,
                            applicationContext = applicationContext
                        )
                    },
                    enabled = state.status != VerificationStatus.Completed
                )
            } else {
                ContinueButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(55.dp),
                    label = "Tomar otra foto",
                    labelStyle = MaterialTheme.typography.labelSmall,
                    labelColor = MaterialTheme.colorScheme.onPrimary,
                    onClick = { viewModel.discardPhoto() },
                    enabled = !state.isUploading,
                )
                ContinueButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(55.dp),
                    label = if (state.isUploading) "Subiendo..." else "Confirmar foto",
                    labelStyle = MaterialTheme.typography.labelSmall,
                    labelColor = MaterialTheme.colorScheme.onPrimary,
                    onClick = { viewModel.uploadConfirmedPhoto(onSuccess = onContinueClick) },
                    enabled = !state.isUploading,
                )
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PermissionRationale(cameraPermissionState: com.google.accompanist.permissions.PermissionState) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val textToShow = if (cameraPermissionState.status.shouldShowRationale) {
            "Necesitamos tu permiso para acceder a tu cámara y verificar tu identidad. Danos permiso y sigamos con el proceso."
        } else {
            "Necesitamos tu permiso para acceder a tu cámara y verificar tu identidad. Danos permiso y sigamos con el proceso. 🎉"
        }

        Text(textToShow)

        ContinueButton(
            modifier = Modifier
                .padding(top = 16.dp)
                .height(48.dp),
            label = "Permitir acceso a la cámara",
            labelStyle = MaterialTheme.typography.labelSmall,
            labelColor = MaterialTheme.colorScheme.onPrimary,
            onClick = { cameraPermissionState.launchPermissionRequest() },
            enabled = true
        )
    }
}