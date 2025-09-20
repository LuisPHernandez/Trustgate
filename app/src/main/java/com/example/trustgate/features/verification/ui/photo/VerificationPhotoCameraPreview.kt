package com.example.trustgate.features.verification.ui.photo

import android.net.Uri
import androidx.camera.compose.CameraXViewfinder
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun VerificationPhotoCameraPreview(
    modifier: Modifier = Modifier,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onContinue: (Uri) -> Unit
) {
    val viewModel: VerificationPhotoViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val surfaceRequest by viewModel.surfaceRequest.collectAsStateWithLifecycle()
    val photoUri by viewModel.photoUri.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(lifecycleOwner) {
        viewModel.bindToCamera(context.applicationContext, lifecycleOwner)
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Viewfinder appears when CameraX asks us for a Surface
        surfaceRequest?.let { req ->
            CameraXViewfinder(
                surfaceRequest = req,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Bottom controls overlay
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
        ) {
            // Take Photo
            Button(
                onClick = {
                    // call VM; result arrives via photoUri StateFlow and callback
                    viewModel.takePhoto(context.applicationContext) {}
                },
                enabled = surfaceRequest != null && photoUri == null
            ) {
                Text("Take photo")
            }

            // Continue (enabled only after we have a photo)
            Button(
                onClick = { photoUri?.let(onContinue) },
                enabled = photoUri != null
            ) {
                Text("Continue")
            }
        }
    }
}