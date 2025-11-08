package com.example.trustgate.presentation.features.signup.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trustgate.core.ui.components.ComposedTextButton
import com.example.trustgate.core.ui.components.ContinueButton
import com.example.trustgate.core.ui.components.PersonalizedTextField
import com.example.trustgate.core.ui.components.SmallSectionSpacer
import com.example.trustgate.core.ui.components.TitleText
import com.example.trustgate.presentation.features.auth.AuthViewModel
import androidx.compose.runtime.getValue //
import androidx.compose.runtime.setValue //
import com.example.trustgate.presentation.features.auth.AuthUiState


@Preview(showBackground = true)
@Composable
fun SignupScreen(
    authViewModel: AuthViewModel = viewModel(),
    onLoginClick: () -> Unit = {},
    onSignupSuccess:()  -> Unit ={}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    //verficair el estado de auth (exitoso o error)
    val authState by authViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }


    LaunchedEffect(authState) {
        when(val state = authState){
            is AuthUiState.Success -> {
                onSignupSuccess()
            }
            is AuthUiState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                authViewModel.resetState()
            }
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
            TitleText(
                text = "Crear cuenta"
            )

            SmallSectionSpacer()

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

            SmallSectionSpacer()

            ContinueButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                label = "Registrarse",
                labelStyle = MaterialTheme.typography.labelSmall,
                labelColor = MaterialTheme.colorScheme.onPrimary,
                onClick = {
                    authViewModel.register(email, password)
                } ,
                enabled = authState !is AuthUiState.Loading
            )

            Spacer(modifier = Modifier.weight(1f))

            ComposedTextButton(
                modifier = Modifier.navigationBarsPadding(),
                staticText = "¿Ya tienes una cuenta?",
                buttonText = "Inicia Sesión",
                onClick = onLoginClick
            )
        }
    }
}