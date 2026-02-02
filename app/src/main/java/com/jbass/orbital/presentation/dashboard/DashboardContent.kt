package com.jbass.orbital.presentation.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.jbass.orbital.domain.model.ConnectionState
import com.jbass.orbital.domain.model.device.DeviceCategory
import com.jbass.orbital.domain.model.device.SmartDevice
import com.jbass.orbital.presentation.dashboard.components.BottomNavBar
import com.jbass.orbital.presentation.dashboard.components.CategoryControlPanel
import com.jbass.orbital.presentation.dashboard.components.DashboardBackground
import com.jbass.orbital.presentation.dashboard.components.DiscoveryRipple
import com.jbass.orbital.presentation.dashboard.components.ManualConnectionDialog
import com.jbass.orbital.presentation.dashboard.components.MinimalistDashboard
import com.jbass.orbital.presentation.dashboard.components.RoomSelectionOverlay
import com.jbass.orbital.presentation.dashboard.components.TopTabs
import com.jbass.orbital.presentation.util.MockData
import com.jbass.orbital.ui.theme.OrbitalTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardContent(
    state: DashboardUiState,
    onFilter: (DeviceCategory) -> Unit,
    onToggleDevice: (SmartDevice) -> Unit,
    onLevelChange: (SmartDevice, Float) -> Unit,
    onRoomSelected:(String) -> Unit,
    onManualIpEntered: (String) -> Unit,
    onCloseCategory: () -> Unit,
    onShowManualInput: () -> Unit,
){

    var isRoomOverlayVisible by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Box(modifier = Modifier
        .fillMaxSize()){


        // Show loading if connecting and no devices yet
        if (state.isLoading && state.devices.isEmpty()) {
            DiscoveryRipple(
                statusText = when (state.connectionState) {
                    is ConnectionState.Reconnecting -> "Reconnecting..."
                    else -> "Scanning Local Network..."
                },
                showManualInput = { onShowManualInput() }
            )

        } else {
            // 1. Root Container (The "Canvas")
            DashboardBackground {

                // --- LAYER 1: Scrolling Content ---
                MinimalistDashboard(state)

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
                        onFilter = { onFilter(it!!) }
                    )
                }

                // The Control Popup
                if (state.selectedCategory != null) {
                    ModalBottomSheet(
                        onDismissRequest = { onCloseCategory() },
                        sheetState = sheetState,
                        containerColor = Color.Transparent, // We will use our own glass background
                        scrimColor = Color.Black.copy(alpha = 0.4f)
                    ) {
                        CategoryControlPanel(
                            category = state.selectedCategory,
                            devices = state.filteredDevices,
                            onToggle = {onToggleDevice(it)},
                            onValueChange = {device, level -> onLevelChange(device,level)}
                        )
                    }
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
                            onRoomSelected(room)
                        },
                        onClose = { isRoomOverlayVisible = false }
                    )
                }
            }

        }

        if (state.showManualInput) {
            ManualConnectionDialog(
                onConnect = { ip -> onManualIpEntered(ip) }
            )
        }
    }
}

@Preview(name = "1. Dashboard Glance", group = "Main", showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
fun PreviewDashboardGlance() {
    OrbitalTheme(darkTheme = true) {
        DashboardContent(
            state = MockData.previewState,
            onFilter = {},
            onToggleDevice = {},
            onLevelChange = { _, _ -> },
            onRoomSelected = {},
            onManualIpEntered = {},
            onCloseCategory = {},
            onShowManualInput = {},
        )
    }
}

@Preview(name = "2. Discovery Phase", group = "System", showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PreviewDiscoveryPhase() {
    OrbitalTheme(darkTheme = true) {
        DashboardContent(
            state = MockData.previewState.copy(
                isLoading = true,
                devices = emptyList(), // Forces ripple to show
                connectionState = ConnectionState.Connecting
            ),
            onFilter = {},
            onToggleDevice = {},
            onLevelChange = { _, _ -> },
            onRoomSelected = {},
            onManualIpEntered = {},
            onCloseCategory = {},
            onShowManualInput = {},
        )
    }
}

@Preview(name = "3. Lighting Controls", group = "Popups", showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
fun PreviewLightingPanel() {
    OrbitalTheme(darkTheme = true) {
        // We wrap it in a Box to simulate the bottom of the screen
        Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
            CategoryControlPanel(
                category = DeviceCategory.LIGHTING,
                devices = MockData.Devices.filter { it.type.category == DeviceCategory.LIGHTING },
                onToggle = {},
                onValueChange = { _, _ -> }
            )
        }
    }
}