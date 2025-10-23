package com.example.trustgate.presentation.features.verification.ui.photo

import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.trustgate.core.ui.components.ContinueButton
import com.example.trustgate.domain.model.VerificationStatus
import com.example.trustgate.presentation.features.verification.viewmodel.VerificationViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun VerificationPhotoScreen(
    viewModel: VerificationViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onContinue: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.error) {
        viewModel.error?.let { snackbarHostState.showSnackbar(it) }
    }

    val applicationContext = LocalContext.current
    val controller = remember {
        LifecycleCameraController(applicationContext).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE
            )
        }
    }

    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    if (cameraPermissionState.status.isGranted) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.7f)
                    .padding(20.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.medium
            ) {
                VerificationPhotoCameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    controller
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ContinueButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(55.dp),
                    label = if (viewModel.isVerifying) "Verificando..." else "Tomar Foto",
                    labelStyle = MaterialTheme.typography.labelSmall,
                    labelColor = MaterialTheme.colorScheme.onPrimary,
                    onClick = {
                        viewModel.takePhoto(
                            controller = controller,
                            onPhotoTaken = viewModel::onTakePhoto,
                            applicationContext = applicationContext
                        )
                    },
                    enabled = if (viewModel.status == VerificationStatus.Completed) false else true,

                )

                ContinueButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(55.dp),
                    label = "Continuar",
                    labelStyle = MaterialTheme.typography.labelSmall,
                    labelColor = MaterialTheme.colorScheme.onPrimary,
                    enabled = if (viewModel.status == VerificationStatus.Completed) true else false,
                    onClick = onContinue
                )
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize()
                .widthIn(max = 480.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val textToShow = if (cameraPermissionState.status.shouldShowRationale) {
                // If the user has denied the permission
                "Necesitamos tu permiso para acceder a tu cámara y verificar tu identidad. " +
                        "Danos permiso y sigamos con el proceso."
            } else {
                // If it's the first time the user lands on this feature
                "Necesitamos tu permiso para acceder a tu cámara y verificar tu identidad. " +
                        "Danos permiso y sigamos con el proceso. \uD83C\uDF89"
            }

            Text(textToShow, textAlign = TextAlign.Center)

            Spacer(Modifier.height(16.dp))

            Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                Text("Permitir acceso a la cámara")
            }
        }
    }
}