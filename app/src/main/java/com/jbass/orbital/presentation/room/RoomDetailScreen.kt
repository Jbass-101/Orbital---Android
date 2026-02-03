package com.jbass.orbital.presentation.room


import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jbass.orbital.R
import com.jbass.orbital.domain.model.device.DeviceCategory
import com.jbass.orbital.domain.model.device.SmartDevice
import com.jbass.orbital.presentation.components.DeviceCard
import com.jbass.orbital.presentation.components.BottomNavBar
import com.jbass.orbital.presentation.components.OrbitalBackground
import com.jbass.orbital.presentation.room.components.SceneSelectionRow
import com.jbass.orbital.presentation.util.MockData
import com.jbass.orbital.ui.theme.OrbitalTheme

@Composable
fun RoomDetailScreen(
    roomName: String,
    roomImageRes: Int = R.drawable.dashboard_bg, // Ideally dynamic based on room
    devices: List<SmartDevice>,
    onBackClick: () -> Unit,
    onToggleDevice: (SmartDevice) -> Unit,
    onLevelChange: (SmartDevice, Float) -> Unit
) {
    // 1. Local state for filtering
    var selectedCategory by remember { mutableStateOf<DeviceCategory?>(null) }

    // 2. Extract only categories present in THIS room
    val availableCategories = remember(devices) {
        devices.map { it.type.category }.distinct().sortedBy { it.name }
    }

    // 3. Filter the device list
    val filteredDevices = remember(selectedCategory, devices) {
        if (selectedCategory == null) devices
        else devices.filter { it.type.category == selectedCategory }
    }

    val roomScenes = listOf("Relax", "Movie", "Read", "Bright")
    var activeScene by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)) {

        // 2. The Content
        OrbitalBackground(
            roomImageRes
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                // Header with Back Button
                RoomDetailHeader(
                    roomName = roomName,
                    onBackClick = onBackClick
                )

                // High-Density Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 140.dp,
                        top = 10.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {

                    item(span = { GridItemSpan(2) }) {
                        // Logic for scenes (ideally passed from ViewModel)
                        val roomScenes = listOf("Relax", "Movie", "Read", "Bright")
                        var activeScene by remember { mutableStateOf<String?>(null) }

                        Column {
                            Text(
                                text = "SCENES",
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    letterSpacing = 3.sp,
                                    color = Color.White.copy(alpha = 0.4f)
                                )
                            )
                            SceneSelectionRow(
                                scenes = roomScenes,
                                selectedScene = activeScene,
                                onSceneClick = { activeScene = it }
                            )
                            Spacer(Modifier.height(16.dp))

                            Text(
                                text = "DEVICES",
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    letterSpacing = 3.sp,
                                    color = Color.White.copy(alpha = 0.4f)
                                )
                            )
                        }
                    }

// Then your items(filteredDevices) logic follows...

                    items(
                        items = filteredDevices,
                        key = {it.id},
                        span = { device ->
                            val isWide = device.type.category == DeviceCategory.MEDIA ||
                                    device.type.category == DeviceCategory.CLIMATE
                            GridItemSpan(if (isWide) 2 else 1)
                        }
                    ) { device ->
                        DeviceCard(
                            device = device,
                            onToggle = { onToggleDevice(device) },
                            onValueChange = { f -> onLevelChange(device, f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateItem()
                        )
                    }
                }

            }

            // LAYER 3: The Hovering Dock
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
            ) {
                BottomNavBar(
                    categories = availableCategories,
                    selectedCategory = selectedCategory,
                    onFilter = { selectedCategory = it }
                )
            }
        }
    }
}

@Composable
fun RoomDetailHeader(
    roomName: String,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 8.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.Rounded.ChevronLeft,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }

        Text(
            text = roomName.uppercase(),
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.ExtraLight,
                letterSpacing = 4.sp,
                color = Color.White
            )
        )
    }
}


@Preview(name = "Room Detail - Living Room", showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
fun PreviewRoomDetail() {
    OrbitalTheme(darkTheme = true) {
        RoomDetailScreen(
            roomName = "Living Room",
            devices = MockData.Devices.filter { it.zoneId == "Living Room" },
            onBackClick = {},
            onToggleDevice = {},
            onLevelChange = { _, _ -> }
        )
    }
}