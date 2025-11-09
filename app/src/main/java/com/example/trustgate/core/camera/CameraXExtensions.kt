package com.example.trustgate.core.camera

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

fun Bitmap.toJpeg(quality: Int = 95): ByteArray {
    val outputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
    return outputStream.toByteArray()
}