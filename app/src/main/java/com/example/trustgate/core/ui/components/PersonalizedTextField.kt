package com.example.trustgate.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun PersonalizedTextField(
    label: String,
    labelStyle: androidx.compose.ui.text.TextStyle,
    labelColor: Color,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false
) {
    val passwordVisible = remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = label,
            style = labelStyle,
            color = labelColor
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(10.dp)),
            shape = RoundedCornerShape(10.dp),
            visualTransformation = if (isPassword && !passwordVisible.value)
                PasswordVisualTransformation()
            else
                VisualTransformation.None,
            trailingIcon = {
                if (isPassword) {
                    val icon = if (passwordVisible.value)
                        Icons.Default.Visibility
                    else
                        Icons.Default.VisibilityOff

                    IconButton(onClick = {
                        passwordVisible.value = !passwordVisible.value
                    }) {
                        Icon(imageVector = icon, contentDescription = null)
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                disabledBorderColor = Color.Gray,
                errorBorderColor = Color.Red,
                focusedContainerColor = MaterialTheme.colorScheme.secondary,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondary
            )
        )
    }
}
