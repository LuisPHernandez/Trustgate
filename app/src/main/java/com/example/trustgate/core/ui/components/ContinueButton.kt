package com.example.trustgate.core.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ContinueButton(
    modifier: Modifier,
    enabled: Boolean = true,
    label: String,
    labelStyle: androidx.compose.ui.text.TextStyle,
    labelColor: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(
            text = label,
            style = labelStyle,
            color = labelColor
        )
    }
}