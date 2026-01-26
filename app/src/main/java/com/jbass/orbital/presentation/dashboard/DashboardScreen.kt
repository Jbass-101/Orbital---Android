package com.jbass.orbital.presentation.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jbass.orbital.domain.model.ConnectionState
import com.jbass.orbital.presentation.components.DeviceCard
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

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
            // THE BENTO GRID
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 160.dp), // Auto-responsive
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(padding)
            ) {
                items(state.devices, key = { it.id }) { device ->
                    DeviceCard(
                        device = device,
                        onToggle = { viewModel.onToggleDevice(device) },
                        onValueChange = { viewModel.onLevelChange(device, it) }
                    )
                }
            }
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