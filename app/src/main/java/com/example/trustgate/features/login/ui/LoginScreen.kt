package com.example.trustgate.features.login.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.trustgate.core.ui.components.PersonalizedTextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.trustgate.R
import com.example.trustgate.core.ui.components.ComposedTextButton
import com.example.trustgate.core.ui.components.ContinueButton
import com.example.trustgate.core.ui.components.TitleText

@Preview(showBackground = true)
@Composable
fun LoginScreen(
    onLoginClick: () -> Unit = {},
    onSignupClick: () -> Unit = {}
) {
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 25.dp, vertical = 65.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "My logo",
            modifier = Modifier.size(156.dp),
            contentScale = ContentScale.Crop
        )

        TitleText(
            text = "Trustgate",
        )

        Text(
            text = "Bienvenido de nuevo. Inicia sesión para continuar.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        PersonalizedTextField(
            label = "Correo electrónico",
            labelStyle = MaterialTheme.typography.labelSmall,
            labelColor = MaterialTheme.colorScheme.onSecondary,
            value = email
        ) { email = it }

        PersonalizedTextField(
            label = "Password",
            labelStyle = MaterialTheme.typography.labelSmall,
            labelColor = MaterialTheme.colorScheme.onSecondary,
            value = password
        ) { password = it }

        Text(
            text = "Olvidaste tu contraseña?",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onTertiary,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End
        )

        ContinueButton(
            label = "Iniciar Sesión",
            labelStyle = MaterialTheme.typography.labelSmall,
            labelColor = MaterialTheme.colorScheme.onPrimary,
            onClick = onLoginClick
        )

        Spacer(modifier = Modifier.height(20.dp))

        ComposedTextButton(
            staticText = "¿No tienes una cuenta?",
            buttonText = "Regístrate",
            onClick = onSignupClick
        )
    }
}