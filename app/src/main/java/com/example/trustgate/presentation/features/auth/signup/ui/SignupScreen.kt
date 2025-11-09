package com.example.trustgate.presentation.features.auth.signup.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trustgate.core.ui.components.ComposedTextButton
import com.example.trustgate.core.ui.components.ContinueButton
import com.example.trustgate.core.ui.components.PersonalizedTextField
import com.example.trustgate.core.ui.components.TitleText
import com.example.trustgate.presentation.features.auth.AuthViewModel
import androidx.compose.runtime.getValue //
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue //
import com.example.trustgate.presentation.features.auth.AuthUiState

@Composable
fun SignupScreen(
    viewModel: AuthViewModel,
    onLoginClick: () -> Unit = {},
    onSignupSuccess:()  -> Unit ={}
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("") }

    // Crear un estado para el Snackbar
    val snackbarHostState = remember { SnackbarHostState() }

    //  Leer el estado de autenticación
    val state by viewModel.uiState.collectAsState()

    // Observamos cambios en el estado de autenticación
    LaunchedEffect(state) {
        when (val s = state){
            // Cuando el signup es exitoso, navegamos a la siguiente pantalla
            is AuthUiState.Success -> {
                viewModel.resetState() // Resetea el estado a idle
                onSignupSuccess()
            }
            // Cuando el signup falla, mostramos el error en un esnackbar
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
                .padding(top = 100.dp, start = 25.dp, end = 25.dp, bottom = 75.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            TitleText(text = "Crear cuenta")

            PersonalizedTextField(
                label = "Nombre",
                labelStyle = MaterialTheme.typography.labelSmall,
                labelColor = MaterialTheme.colorScheme.onSecondary,
                value = name,
                onValueChange = { name = it }
            )

            PersonalizedTextField(
                label = "Correo electrónico",
                labelStyle = MaterialTheme.typography.labelSmall,
                labelColor = MaterialTheme.colorScheme.onSecondary,
                value = email,
                onValueChange = { email = it }
            )

            PersonalizedTextField(
                label = "Contraseña",
                labelStyle = MaterialTheme.typography.labelSmall,
                labelColor = MaterialTheme.colorScheme.onSecondary,
                value = password,
                onValueChange = { password = it }
            )

            ContinueButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                label = if (state is AuthUiState.Loading) "Cargando..." else "Registrarse",
                labelStyle = MaterialTheme.typography.labelSmall,
                labelColor = MaterialTheme.colorScheme.onPrimary,
                onClick = {
                    viewModel.signup(name, email, password) // Hacemos signup en Firebase
                } ,
                enabled = state !is AuthUiState.Loading
            )

            ComposedTextButton(
                modifier = Modifier.navigationBarsPadding(),
                staticText = "¿Ya tienes una cuenta?",
                buttonText = "Inicia Sesión",
                onClick = onLoginClick // Navegamos a la pantalla de login
            )
        }
    }
}