package com.jbass.orbital.presentation.room


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jbass.orbital.domain.model.device.DeviceCategory
import com.jbass.orbital.domain.model.device.SmartDevice
import com.jbass.orbital.presentation.components.DeviceCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomCategoryDevicesScreen(
    roomName: String,
    category: DeviceCategory,
    devices: List<SmartDevice>,
    onToggle: (SmartDevice) -> Unit,
    onLevelChange: (SmartDevice, Float) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$roomName · ${category.name}") }
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(160.dp),
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(devices, key = { it.id }) { device ->
                DeviceCard(
                    device = device,
                    onToggle = { onToggle(device) },
                    onValueChange = { onLevelChange(device, it) }
                )
            }
        }
    }
}
