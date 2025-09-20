package com.example.trustgate.features.verification.ui.photo

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun VerificationPhotoScreen(
    modifier: Modifier = Modifier,
    onContinue: (Uri) -> Unit
) {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    if (cameraPermissionState.status.isGranted) {
        VerificationPhotoCameraPreview(modifier, onContinue = onContinue)
    } else {
        Column(
            modifier = modifier.fillMaxSize().wrapContentSize().widthIn(max = 480.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val textToShow = if (cameraPermissionState.status.shouldShowRationale) {
                // If the user has denied the permission but the rationale can be shown
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