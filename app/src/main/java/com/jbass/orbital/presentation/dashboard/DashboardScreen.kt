package com.jbass.orbital.presentation.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jbass.orbital.domain.model.ConnectionState
import com.jbass.orbital.domain.model.device.DeviceCategory
import com.jbass.orbital.domain.model.device.SmartDevice
import com.jbass.orbital.presentation.components.DeviceCard
import com.jbass.orbital.presentation.components.WeatherCard
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    onRoomsClick : () -> Unit
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

    DashboardContent(
        state = state,
        onFilter = viewModel::onFilter,
        onToggleDevice = viewModel::onToggleDevice,
        onLevelChange = viewModel::onLevelChange,
        onRoomSelected = viewModel::onRoomSelected,
        onManualIpEntered = viewModel::onManualIpEntered,
        onCloseCategory = viewModel::onCloseCategoryControl,
        onShowManualInput = viewModel::showManualInput
    )

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

@Composable
fun DashboardGrid(
    state: DashboardUiState,
    onToggleDevice: (SmartDevice) -> Unit,
    contentPadding: PaddingValues
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // 2 columns for standard tiles
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        // 1. Weather Card (Hero - Span 2)
        item(span = { GridItemSpan(2) }) {
            WeatherCard(state.weather)
        }

        // 2. Room Label (Span 2)
        item(span = { GridItemSpan(2) }) {
            Text(
                text = state.selectedRoomId ?: "All Zones",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Light,
                color = Color.White,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // 3. Device Items
        items(
            items = state.devices,
            key = { it.id },
            span = { device ->
                // Make climate devices take full width (Hero), others take 1 column
                val isHero = device.type.category == DeviceCategory.CLIMATE
                GridItemSpan(if (isHero) 2 else 1)
            }
        ) { device ->
            DeviceCard(
                device = device,
                onToggle = { onToggleDevice(device) },
                onValueChange = {}
            )
        }

        // Bottom Spacer for the hovering nav bar
        item(span = { GridItemSpan(2) }) {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}