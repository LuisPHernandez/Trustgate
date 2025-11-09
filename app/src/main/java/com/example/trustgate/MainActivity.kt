package com.example.trustgate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.trustgate.core.common.RepositoryProvider
import com.example.trustgate.core.ui.theme.TrustgateTheme
import com.example.trustgate.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RepositoryProvider.init(application)
        enableEdgeToEdge()
        setContent {
            TrustgateTheme(dynamicColor = false) {
                Surface(
                    modifier = Modifier.Companion
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}