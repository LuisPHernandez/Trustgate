package com.example.trustgate.presentation.features.verification.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trustgate.domain.model.VerificationStatus
import com.example.trustgate.domain.repo.VerificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.collections.plus

class VerificationViewModel(
    private val verification: VerificationRepository
) : ViewModel() {
    // CONSENT
    var consent by mutableStateOf(false)
    var status by mutableStateOf<VerificationStatus?>(null)

    fun onConsentChange() {
        if (!consent) {
            viewModelScope.launch {
                status = verification.giveConsent()
            }
        }
        consent = !consent
    }

    // PHOTO
    private val _bitmaps = MutableStateFlow<List<Bitmap>>(emptyList())
    var isVerifying by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    fun takePhoto(
        controller: LifecycleCameraController,
        onPhotoTaken: (Bitmap) -> Unit,
        applicationContext: Context
    ) {
        controller.takePicture(
            ContextCompat.getMainExecutor(applicationContext),
            object: ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)

                    onPhotoTaken(image.toBitmap())
                    submitFakePhoto()
                    image.close()
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.e("Camera", "Couldn't take photo: ", exception)
                }
            }
        )
    }

    fun submitFakePhoto() {
        viewModelScope.launch {
            try {
                isVerifying = true
                error = null
                val fakeUri = "photo-${System.currentTimeMillis()}"
                status = verification.uploadIdPhoto(fakeUri)
            } catch (e: Exception) {
                error = e.message ?: "Error al verificar la identidad"
            } finally {
                isVerifying = false
            }
        }
    }

    fun onTakePhoto(bitmap: Bitmap) {
        _bitmaps.value += bitmap
    }
}