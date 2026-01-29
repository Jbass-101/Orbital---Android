package com.jbass.orbital

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import com.jbass.orbital.presentation.dashboard.DashboardScreen
import com.jbass.orbital.presentation.navigation.OrbitalNavHost
import com.jbass.orbital.ui.theme.OrbitalTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OrbitalTheme {
                OrbitalNavHost()
            }
        }
    }
}