package com.example.trustgate.presentation.features.auth.login.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.trustgate.core.ui.components.PersonalizedTextField
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.trustgate.R
import com.example.trustgate.core.ui.components.ComposedTextButton
import com.example.trustgate.core.ui.components.ContinueButton
import com.example.trustgate.core.ui.components.TitleText
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.trustgate.core.ui.components.CheckboxRow
import com.example.trustgate.presentation.features.auth.AuthUiState
import com.example.trustgate.presentation.features.auth.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit = {},
    onSignupClick: () -> Unit = {}
) {
    // DataStore de información de Login/Signup/Verification
    val onboardingInfo = viewModel.onboardingInfo

    // Email guardado para agilizar Login
    val savedEmail by onboardingInfo.lastLoggedInEmail.collectAsState(initial = "")

    // Booleano para saber si el usuario quiere seguir conectado
    val keepSignedIn by onboardingInfo.keepSignedIn.collectAsState(initial = false)

    var email by rememberSaveable { mutableStateOf( "") }
    var password by rememberSaveable { mutableStateOf("") }

    // Crear un estado para el Snackbar
    val snackbarHostState = remember { SnackbarHostState() }

    // Leer el estado de autenticación
    val state by viewModel.uiState.collectAsState()

    // Insertamos savedEmail en email
    LaunchedEffect(savedEmail) {
        if (email.isBlank() && !savedEmail.isNullOrBlank()) {
            email = savedEmail ?: ""
        }
    }

    // Observamos cambios en el estado de autenticación
    LaunchedEffect(state) {
        when (val s = state) {
            // Cuando el login es exitoso, navegamos a la siguiente pantalla
            is AuthUiState.Success -> {
                viewModel.resetState() // Resetea el estado a idle
                onLoginSuccess()
            }
            // Cuando el login falla, mostramos el error en un esnackbar
            is AuthUiState.Error -> {
                snackbarHostState.showSnackbar(s.message)
                viewModel.resetState() // Resetea el estado a idle
            }
            // No hacemos nada en los estados Idle o Loading
            else -> Unit
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 65.dp, start = 25.dp, end = 25.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "My logo",
                modifier = Modifier.size(156.dp),
                contentScale = ContentScale.Crop
            )

            TitleText(text = "Trustgate",)

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
                value = email,
                onValueChange = {email = it},
            )

            PersonalizedTextField(
                label = "Contraseña",
                labelStyle = MaterialTheme.typography.labelSmall,
                labelColor = MaterialTheme.colorScheme.onSecondary,
                value = password,
                onValueChange = {password = it},
                isPassword = true
            )

            CheckboxRow(
                label = "Seguir Conectado",
                checked = keepSignedIn,
                onCheckedChange = { checked -> viewModel.setKeepSignedIn(checked) },
                labelStyle = MaterialTheme.typography.labelSmall,
                labelColor = MaterialTheme.colorScheme.onSecondary,
            )

            ContinueButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                label = if (state is AuthUiState.Loading) "Cargando..." else "Iniciar sesión",
                labelStyle = MaterialTheme.typography.labelSmall,
                labelColor = MaterialTheme.colorScheme.onPrimary,
                onClick = {
                    viewModel.login(email, password) // Hacemos login en Firebase
                },
                enabled = state !is AuthUiState.Loading
            )

            ComposedTextButton(
                staticText = "¿No tienes una cuenta?",
                buttonText = "Regístrate",
                onClick = onSignupClick // Navegamos a la pantalla de signup
            )
        }
    }
}