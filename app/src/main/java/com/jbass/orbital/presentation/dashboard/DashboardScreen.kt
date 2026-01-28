package com.jbass.orbital.presentation.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.jbass.orbital.domain.model.ConnectionState
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var currentRoute by remember { mutableStateOf("home") }
    // Handle One-Time Errors (Snackbars)
    LaunchedEffect(true) {
        viewModel.uiEvent.collectLatest { event ->
            when(event) {
                is UiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Orbital Home", style = MaterialTheme.typography.headlineMedium)
                        ConnectionBadge(state.connectionState)
                    }
                }
            )
        }
    ) { padding ->

        // Show loading if connecting and no devices yet
        if (state.isLoading && state.devices.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {


            when(currentRoute){
                "home" -> OrbitalDashboard(
                    "JBass_101",
                    "15",
                    listOf("Dinning", "Masterbedroom"),
                    devices = state.devices
                )

            }

            // THE BENTO GRID
//            LazyVerticalGrid(
//                columns = GridCells.Adaptive(minSize = 160.dp), // Auto-responsive
//                contentPadding = PaddingValues(16.dp),
//                horizontalArrangement = Arrangement.spacedBy(12.dp),
//                verticalArrangement = Arrangement.spacedBy(12.dp),
//                modifier = Modifier.padding(padding)
//            ) {
//                items(state.devices, key = { it.id }) { device ->
//                    DeviceCard(
//                        device = device,
//                        onToggle = { viewModel.onToggleDevice(device) },
//                        onValueChange = { viewModel.onLevelChange(device, it) }
//                    )
//                }
//            }
        }
    }
}

@Composable
fun ConnectionBadge(state: ConnectionState) {
    val (text, color) = when (state) {
        ConnectionState.Connected -> "Live" to Color(0xFF4CAF50) // Green
        ConnectionState.Connecting -> "Connecting..." to Color(0xFFFFC107) // Amber
        else -> "Offline" to Color(0xFFF44336) // Red
    }

    Text(
        text = "● $text",
        style = MaterialTheme.typography.labelSmall,
        color = color
    )
}