package com.jbass.orbital.presentation.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jbass.orbital.domain.model.ConnectionState
import com.jbass.orbital.domain.model.DeviceCategory
import com.jbass.orbital.domain.model.SmartDevice
import com.jbass.orbital.presentation.components.DeviceCard
import com.jbass.orbital.presentation.dashboard.components.RoomSelectionOverlay
import com.jbass.orbital.presentation.dashboard.components.BottomNavBar
import com.jbass.orbital.presentation.dashboard.components.DashboardBackground
import com.jbass.orbital.presentation.dashboard.components.DiscoveryRipple
import com.jbass.orbital.presentation.dashboard.components.ManualConnectionDialog
import com.jbass.orbital.presentation.dashboard.components.MinimalistDashboard
import com.jbass.orbital.presentation.dashboard.components.TopTabs
import com.jbass.orbital.presentation.dashboard.components.WeatherCard
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    onRoomsClick : () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var isRoomOverlayVisible by remember { mutableStateOf(false) }

    // Handle One-Time Errors (Snackbars)
    LaunchedEffect(true) {
        viewModel.uiEvent.collectLatest { event ->
            when(event) {
                is UiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }


    Box(modifier = Modifier
        .fillMaxSize()){


        // Show loading if connecting and no devices yet
        if (state.isLoading && state.devices.isEmpty()) {
            DiscoveryRipple(
                statusText = when (state.connectionState) {
                    is ConnectionState.Reconnecting -> "Reconnecting..."
                    else -> "Scanning Local Network..."
                },
                showManualInput = { viewModel.showManualInput() }
            )

        } else {
            // 1. Root Container (The "Canvas")
            DashboardBackground() {

                // --- LAYER 1: Scrolling Content ---
                MinimalistDashboard(state) { }

                // --- LAYER 2: Floating Header (Top Layer) ---
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Black.copy(alpha = 0.4f), Color.Transparent)
                            )
                        )
                        .statusBarsPadding()
                ) {
                    TopTabs(
                        onRoomsClick = { isRoomOverlayVisible = true },
                        onScenesClick = { /* Handle Scenes */ },
                        onSettingsClick = { /* Handle Settings */ }
                    )
                }

                // --- LAYER 3: Floating Glass Dock (Bottom Layer) ---
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .navigationBarsPadding()
                ) {
                    BottomNavBar(
                        categories = DeviceCategory.entries,
                        selectedCategory = state.selectedCategory,
                        onFilter = { viewModel.onFilter(it) }
                    )
                }

                // --- LAYER 4: Room Selection Overlay ---
                AnimatedVisibility(
                    visible = isRoomOverlayVisible,
                    enter = fadeIn() + slideInVertically { it / 2 },
                    exit = fadeOut() + slideOutVertically { it / 2 }
                ) {
                    RoomSelectionOverlay(
                        rooms = state.rooms,
                        onRoomSelected = { room ->
                            viewModel.onRoomSelected(room)
                        },
                        onClose = { isRoomOverlayVisible = false }
                    )
                }
            }

        }

        if (state.showManualInput) {
            ManualConnectionDialog(
                onConnect = { ip -> viewModel.onManualIpEntered(ip) }
            )
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


@Composable
fun LiveStatusCard(title: String, status: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
        Spacer(Modifier.width(16.dp))
        Column {
            Text(title, color = Color.White, style = MaterialTheme.typography.titleSmall)
            Text(status, color = Color.White.copy(alpha = 0.5f), style = MaterialTheme.typography.bodySmall)
        }
    }
}