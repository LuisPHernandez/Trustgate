package com.example.trustgate.core.common

import android.app.Application
import androidx.datastore.preferences.preferencesDataStore
import com.example.trustgate.data.auth.AuthRepositoryImpl
import com.example.trustgate.data.auth.remote.AuthRemoteDataSource
import com.example.trustgate.data.gate.GateRepositoryImpl
import com.example.trustgate.data.local.OnboardingDataStore
import com.example.trustgate.data.verification.VerificationRepositoryImpl
import com.example.trustgate.data.verification.remote.VerificationRemoteDataSource
import com.example.trustgate.domain.repo.AuthRepository
import com.example.trustgate.domain.repo.GateRepository
import com.example.trustgate.domain.repo.VerificationRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlin.getValue

object RepositoryProvider {
    // Init
    private lateinit var app: Application
    fun init(application: Application) { app = application }

    // DataStore de onboarding
    private val Application.dataStore by preferencesDataStore(name = "onboarding")

    // Singleton de AuthRepository
    private val authRepo by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        AuthRepositoryImpl(source = AuthRemoteDataSource())
    }

    // Función que expone AuthRepository
    fun provideAuthRepository(): AuthRepository = authRepo

    // Singleton de VerificationRepository
    private val verificationRepo by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        VerificationRepositoryImpl(source = VerificationRemoteDataSource())
    }

    // Función que expone VerificationRepository
    fun provideVerificationRepository(): VerificationRepository = verificationRepo

    // Singleton de GateRepository
    private val gateRepo by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        GateRepositoryImpl(
            firestore = FirebaseFirestore.getInstance(),
            storage = FirebaseStorage.getInstance(),
            auth = FirebaseAuth.getInstance()
        )
    }

    // Función que expone GateRepository
    fun provideGateRepository(): GateRepository = gateRepo

    // Singleton del OnboardingDataStore
    private val onboardingDataStore: OnboardingDataStore by lazy {
        OnboardingDataStore(app.dataStore)
    }

    // Función que expone OnboardingDataStore
    fun provideOnboardingDataStore(): OnboardingDataStore {
        return onboardingDataStore
    }
}