package com.jbass.orbital.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jbass.orbital.domain.model.DeviceState
import com.jbass.orbital.domain.model.DeviceType
import com.jbass.orbital.domain.model.SmartDevice
import com.jbass.orbital.R

@Composable
fun OrbitalDashboard(
    username: String,
    temperature: String,
    rooms: List<String>,
    devices: List<SmartDevice>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {

        TopHeader(username)

        Spacer(modifier = Modifier.height(20.dp))

        TemperatureCard(temperature)

        Spacer(modifier = Modifier.height(20.dp))

        RoomSelector(rooms)

        Spacer(modifier = Modifier.height(24.dp))

        DeviceGrid(devices)
    }
}

@Composable
private fun TopHeader(username: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Welcome back,",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
            Text(
                text = username,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )
        }

        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Profile",
            modifier = Modifier.size(36.dp),
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun TemperatureCard(temperature: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = "Weather",
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "Indoor Temperature",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Text(
                    text = temperature,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun RoomSelector(rooms: List<String>) {
    var selectedRoom by remember { mutableStateOf(rooms.first()) }

    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(rooms) { room ->
            val isSelected = room == selectedRoom

            Surface(
                modifier = Modifier.clip(CircleShape),
                color = if (isSelected)
                    MaterialTheme.colorScheme.onBackground
                else
                    MaterialTheme.colorScheme.surface,
                onClick = { selectedRoom = room }
            ) {
                Text(
                    text = room,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = if (isSelected)
                        MaterialTheme.colorScheme.background
                    else
                        MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun DeviceCard(
    device: SmartDevice,
    modifier: Modifier = Modifier
) {

    val isActive = when (val s = device.state) {
        is DeviceState.OnOff -> s.isOn
        is DeviceState.Level -> s.value > 0
        is DeviceState.Media -> s.isOn
        else -> false
    }

    var isOn by remember { mutableStateOf(isActive ) }



    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(20.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Icon(
                painter = painterResource(getIconForDevice(device.type)),
                contentDescription = device.name,
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = device.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isOn) "On" else "Off",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                Switch(
                    checked = isOn,
                    onCheckedChange = { isOn = it }
                )
            }
        }
    }
}


@Composable
private fun DeviceGrid(devices: List<SmartDevice>) {

    LazyVerticalGrid(

        columns = GridCells.Adaptive(minSize = 140.dp), // Auto-responsive
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        items(devices,){ device ->
            DeviceCard(device)

        }

    }
//    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
//        devices.chunked(2).forEach { rowDevices ->
//            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
//                rowDevices.forEach { device ->
//                    DeviceCard(
//                        device = device,
//                        modifier = Modifier.weight(1f)
//                    )
//                }
//
//                if (rowDevices.size == 1) {
//                    Spacer(modifier = Modifier.weight(1f))
//                }
//            }
//        }
//    }
}


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













// Helper: Map Backend Enum to UI Icon
private fun getIconForDevice(type: DeviceType): Int {
    return when (type) {
        DeviceType.LIGHT, DeviceType.DIMMER, DeviceType.RGB_LIGHT -> R.drawable.lightbulb
        DeviceType.THERMOSTAT, DeviceType.HVAC, DeviceType.FAN -> R.drawable.device_thermostat
        DeviceType.TV, DeviceType.AVR, DeviceType.SPEAKER -> R.drawable.tv
        DeviceType.DOOR_LOCK, DeviceType.GARAGE_DOOR -> R.drawable.lock
        DeviceType.SMART_PLUG -> R.drawable.power
        else -> R.drawable.devices_other
    }
}

// Helper: Format status text
private fun getStatusText(device: SmartDevice): String {
    return when (val s = device.state) {
        is DeviceState.OnOff -> if (s.isOn) "On" else "Off"
        is DeviceState.Level -> if (s.value > 0) "${s.value}%" else "Off"
        is DeviceState.Temperature -> "Target: ${s.target}°"
        is DeviceState.Media -> if (s.isOn) "Playing" else "Paused"
        is DeviceState.Network -> if (s.online) "Online" else "Offline"
        else -> "Ready"
    }
}




@Preview
@Composable
fun PreviewTopHeader (){
    RoomSelector(listOf("Bedrom","Kitchen"))
}