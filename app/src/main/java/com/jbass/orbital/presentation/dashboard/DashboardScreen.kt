package com.jbass.orbital.presentation.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jbass.orbital.domain.model.ConnectionState
import com.jbass.orbital.domain.model.DeviceCategory
import com.jbass.orbital.domain.model.SmartDevice
import com.jbass.orbital.presentation.components.DeviceCard
import com.jbass.orbital.presentation.components.RoomSelectorButtonRow
import com.jbass.orbital.presentation.components.WeatherCard
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val devicesCategories = extractDeviceCategories(state.devices)
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
        contentWindowInsets = WindowInsets.systemBars,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {

                    ConnectionBadge(state.connectionState)
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
//             THE BENTO GRID
            Column(modifier = Modifier.padding(padding)
            ) {
                WeatherCard(state.weather)
                RoomSelectorButtonRow(devicesCategories, { viewModel.onFilter(it) })
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 160.dp), // Auto-responsive
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.filteredDevices,
                        key = { it.id }) { device ->

                        DeviceCard(
                            device = device,
                            onToggle = { viewModel.onToggleDevice(device) },
                            onValueChange = { viewModel.onLevelChange(device, it) }
                        )
                    }
                }
            }


//            when(currentRoute){
//                "home" -> OrbitalDashboard(
//                    padding,
//                    "JBass_101",
//                    state.weather,
//                    listOf("Dinning", "Master Bedroom","Bedroom","Kitchen", "Braai Area","Bedroom","Kitchen", "Braai Area"),
//                    devices = state.devices
//                )
//
//            }

        }
    }
}

fun extractDeviceCategories(
    devices: List<SmartDevice>
): List<DeviceCategory> =
    DeviceCategory.entries.filter { category ->
        devices.any { it.type.category == category }
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