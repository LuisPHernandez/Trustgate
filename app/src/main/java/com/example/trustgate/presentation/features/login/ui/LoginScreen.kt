package com.example.trustgate.presentation.features.login.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.trustgate.core.ui.components.PersonalizedTextField
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.trustgate.R
import com.example.trustgate.core.ui.components.ComposedTextButton
import com.example.trustgate.core.ui.components.ContinueButton
import com.example.trustgate.core.ui.components.SmallSectionSpacer
import com.example.trustgate.core.ui.components.TitleText
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.trustgate.presentation.features.login.viewmodel.LoginViewModel

@Preview(showBackground = true)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onLoginSuccess: () -> Unit = {},
    onSignupClick: () -> Unit = {}
) {
    val session = viewModel.session
    var navigated by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(session) {
        if (session != null && !navigated) {
            navigated = true
            onLoginSuccess()
        }
    }

    LaunchedEffect(viewModel.error) {
        viewModel.error?.let { snackbarHostState.showSnackbar(it) }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) })
    { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 25.dp, vertical = 75.dp),
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

            SmallSectionSpacer()

            PersonalizedTextField(
                label = "Correo electrónico",
                labelStyle = MaterialTheme.typography.labelSmall,
                labelColor = MaterialTheme.colorScheme.onSecondary,
                value = viewModel.email,
                onValueChange = viewModel::onEmailChange
            )

            PersonalizedTextField(
                label = "Contraseña",
                labelStyle = MaterialTheme.typography.labelSmall,
                labelColor = MaterialTheme.colorScheme.onSecondary,
                value = viewModel.password,
                onValueChange = viewModel::onPasswordChange
            )

            Text(
                text = "Olvidaste tu contraseña?",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onTertiary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )

            SmallSectionSpacer()

            ContinueButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                label = if (viewModel.isLoading) "Inciando Sesión..." else "Iniciar Sesión",
                labelStyle = MaterialTheme.typography.labelSmall,
                labelColor = MaterialTheme.colorScheme.onPrimary,
                onClick = { viewModel.submit() },
                enabled = !viewModel.isLoading
            )

            Spacer(modifier = Modifier.weight(1f))

            ComposedTextButton(
                modifier = Modifier.navigationBarsPadding(),
                staticText = "¿No tienes una cuenta?",
                buttonText = "Regístrate",
                onClick = onSignupClick
            )
        }
    }
}