package com.example.trustgate.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.trustgate.data.auth.AuthRepositoryImpl
import com.example.trustgate.data.auth.simulated.AuthSimulatedDataSource
import com.example.trustgate.data.gate.GateRepositoryImpl
import com.example.trustgate.data.gate.simulated.GateSimulatedDataSource
import com.example.trustgate.presentation.features.auth.AuthViewModel
import com.example.trustgate.presentation.features.home.ui.HomeScreen
import com.example.trustgate.presentation.features.home.viewmodel.HomeViewModel
import com.example.trustgate.presentation.features.home.viewmodel.HomeViewModelFactory
import com.example.trustgate.presentation.features.success.ui.SuccessScreen
import com.example.trustgate.presentation.features.login.ui.LoginScreen
import com.example.trustgate.presentation.features.login.viewmodel.LoginViewModel
import com.example.trustgate.presentation.features.login.viewmodel.LoginViewModelFactory
import com.example.trustgate.presentation.features.signup.ui.SignupScreen
import com.example.trustgate.presentation.features.verification.navigation.VerificationNavigation

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = AppScreens.Login.route
    ) {
        composable(AppScreens.Login.route) {
            val authViewModel: AuthViewModel = viewModel()


            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(AppScreens.Home.route) {
                        popUpTo(AppScreens.Login.route) { inclusive = true }
                    }
                },
                onSignupClick = {
                    navController.navigate(AppScreens.Signup.route)
                }
            )
        }

        composable(AppScreens.Signup.route) {
            val authViewModel: AuthViewModel = viewModel()
            SignupScreen(
                authViewModel = authViewModel,
                onSignupSuccess = {
                    navController.navigate(AppScreens.Verification.route) {
                        //se limpia para que no se pueda apachar la flechita atras y regresar
                        popUpTo(AppScreens.Login.route) { inclusive = true }
                    }
                },
                onLoginClick = {
                    //regresa al login
                    navController.popBackStack()
                }
            )
        }

        composable(AppScreens.Verification.route) {
            VerificationNavigation(
                onFinish = {
                    navController.navigate(AppScreens.Home.route) {
                        popUpTo(AppScreens.Verification.route) { inclusive = true }
                    }
                }
            )
        }

        composable(AppScreens.Home.route) {
            val gateRepo = remember { GateRepositoryImpl(GateSimulatedDataSource()) }
            val vm: HomeViewModel = viewModel(factory = HomeViewModelFactory(gateRepo))

            HomeScreen(
                viewModel = vm,
                onLogoutClick = {
                    navController.navigate(AppScreens.Login.route) {
                        popUpTo(AppScreens.Home.route) { inclusive = true }
                    }
                },
                onSuccess = { gateName ->
                    navController.navigate(AppScreens.Success.route)
                }
            )
        }

        composable(AppScreens.Success.route) {
            SuccessScreen(onGoHome = {
                navController.navigate(AppScreens.Home.route){
                    popUpTo(AppScreens.Success.route) { inclusive = true }
                    launchSingleTop = true
                    }
                }
            )
        }
    }
}