package com.example.trustgate.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OnboardingDataStore(private val dataStore: DataStore<Preferences>) {
    // Keys
    private val LAST_LOGGED_IN_EMAIL = stringPreferencesKey("last_logged_in_email")

    // Get flows
    val lastLoggedInEmail: Flow<String?> = dataStore.data.map { data ->
        data[LAST_LOGGED_IN_EMAIL]
    }

    // Set methods
    suspend fun setLastLoggedInEmail(email: String) {
        dataStore.edit { it[LAST_LOGGED_IN_EMAIL] = email }
    }
}