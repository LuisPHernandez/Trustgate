package com.example.trustgate.features.signup.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.trustgate.core.ui.components.ComposedTextButton
import com.example.trustgate.core.ui.components.ContinueButton
import com.example.trustgate.core.ui.components.PersonalizedTextField
import com.example.trustgate.core.ui.components.SmallSectionSpacer
import com.example.trustgate.core.ui.components.TitleText

@Preview(showBackground = true)
@Composable
fun SignupScreen(
    onSignupClick: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp, start = 25.dp, end = 25.dp, bottom = 75.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        TitleText(
            text = "Crear cuenta"
        )

        SmallSectionSpacer()

        PersonalizedTextField(
            label = "Nombre",
            labelStyle = MaterialTheme.typography.labelSmall,
            labelColor = MaterialTheme.colorScheme.onSecondary,
            value = name
        ) { name = it }

        PersonalizedTextField(
            label = "Correo electrónico",
            labelStyle = MaterialTheme.typography.labelSmall,
            labelColor = MaterialTheme.colorScheme.onSecondary,
            value = email
        ) { email = it }

        PersonalizedTextField(
            label = "Contraseña",
            labelStyle = MaterialTheme.typography.labelSmall,
            labelColor = MaterialTheme.colorScheme.onSecondary,
            value = password
        ) { password = it }

        SmallSectionSpacer()

        ContinueButton(
            label = "Registrarse",
            labelStyle = MaterialTheme.typography.labelSmall,
            labelColor = MaterialTheme.colorScheme.onPrimary,
            onClick = onSignupClick
        )

        Spacer(modifier = Modifier.weight(1f))

        ComposedTextButton(
            staticText = "¿Ya tienes una cuenta?",
            buttonText = "Inicia Sesión",
            onClick = onSignupClick
        )
    }
}