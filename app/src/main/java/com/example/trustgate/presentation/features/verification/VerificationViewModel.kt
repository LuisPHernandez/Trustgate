package com.example.trustgate.presentation.features.verification

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trustgate.core.camera.toJpeg
import com.example.trustgate.domain.repo.VerificationRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VerificationViewModel(
    private val repo: VerificationRepository,
): ViewModel() {
    private val _state = MutableStateFlow(VerificationUiState())
    val state: StateFlow<VerificationUiState> = _state.asStateFlow()

    fun takePhoto(
        controller: LifecycleCameraController,
        applicationContext: Context
    ) {
        controller.takePicture(
            ContextCompat.getMainExecutor(applicationContext),
            object: ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)
                    try {
                        val bmp = image.toBitmap()
                        val rotation = image.imageInfo.rotationDegrees
                        val rotated = bmp.rotate(rotation.toFloat())
                        _state.update { it.copy(lastPhoto = rotated) }
                    } catch (e: Exception) {
                        _state.update { it.copy(error = e.message ?: "No se pudo tomar la foto") }
                    } finally {
                        image.close()
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.e("Camera", "Couldn't take photo: ", exception)
                    _state.update { it.copy(error = "No se pudo tomar la foto") }
                }
            }
        )
    }

    private fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = android.graphics.Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }

    fun discardPhoto() {
        _state.update { it.copy(lastPhoto = null, error = null) }
    }

    fun onConsentChange() {
        if (!state.value.consent) {
            giveConsent()
        } else {
            _state.update { it.copy(consent = !it.consent) }
        }
    }


    fun giveConsent() {
        viewModelScope.launch {
            try {
                _state.update {
                    it.copy(isLoading = true, status = repo.giveConsent(), consent = true)
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(error = e.message ?: "Error al guardar consentimiento", consent = false)
                }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun uploadConfirmedPhoto(onSuccess: () -> Unit) {
        val bmp = state.value.lastPhoto ?: run {
            _state.update { it.copy(error = "No hay foto para subir") }
            return
        }
        viewModelScope.launch {
            try {
                _state.update { it.copy(isUploading = true, error = null) }
                val bytes = bmp.toJpeg(quality = 90)
                val user = FirebaseAuth.getInstance().currentUser
                val uid = user?.uid
                _state.update { it.copy(status = repo.uploadIdPhoto(uid!!, bytes)) }
                onSuccess()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message ?: "Error al subir la fotografía") }
            } finally {
                _state.update { it.copy(isUploading = false) }
            }
        }
    }
}