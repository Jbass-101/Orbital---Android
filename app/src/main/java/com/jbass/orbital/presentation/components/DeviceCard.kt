package com.jbass.orbital.presentation.components

import android.graphics.drawable.Icon
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jbass.orbital.domain.model.DeviceState
import com.jbass.orbital.domain.model.DeviceType
import com.jbass.orbital.domain.model.SmartDevice
import com.jbass.orbital.R
import com.jbass.orbital.domain.model.DeviceMetadata
import com.jbass.orbital.ui.theme.OrbitalTheme

@Composable
fun DeviceCard(
    device: SmartDevice,
    onToggle: () -> Unit,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    showFavorite: Boolean = true
) {
    val isActive = device.metadata.isReachable
    val rotation by animateFloatAsState(
        targetValue = if (isActive) 0f else 180f,
        label = "iconRotation"
    )

    val cardColor by animateColorAsState(
        targetValue = if (isActive)
            MaterialTheme.colorScheme.primaryContainer
        else
            MaterialTheme.colorScheme.surfaceVariant,
        label = "cardColor"
    )

    val iconColor by animateColorAsState(
        targetValue = if (isActive)
            MaterialTheme.colorScheme.onPrimaryContainer
        else
            MaterialTheme.colorScheme.onSurfaceVariant,
        label = "iconColor"
    )

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = cardColor,
        tonalElevation = if (isActive) 2.dp else 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onToggle() }
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header: Icon & Favorite
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Device Icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                            RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_launcher_foreground),
//                        imageVector = getIconForDevice(device.type),
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier
                            .size(28.dp)
                            .rotate(rotation)
                    )
                }

                // Favorite Star
//                if (showFavorite) {
//                    IconButton(
//                        onClick = { /* Toggle favorite */ },
//                        modifier = Modifier.size(24.dp)
//                    ) {
//                        Icon(
//                            imageVector = if (device.isFavorite)
//                                Icons.Rounded.Star
//                            else
//                                Icons.Rounded.StarOutline,
//                            contentDescription = "Favorite",
//                            tint = MaterialTheme.colorScheme.primary,
//                            modifier = Modifier.size(20.dp)
//                        )
//                    }
//                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Body: Controls based on device type
            when (val state = device.state) {
                is DeviceState.Level -> {
                    Column {
                        Slider(
                            value = state.value.toFloat(),
                            onValueChange = { newValue ->
                                onValueChange(newValue)
                            },
                            valueRange = 0f..100f,
                            colors = SliderDefaults.colors(
                                thumbColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                activeTrackColor = iconColor,
                                inactiveTrackColor = iconColor.copy(alpha = 0.3f)
                            ),
                            modifier = Modifier.height(24.dp)
                        )
                        Text(
                            text = "${state.value}%",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = iconColor,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
                is DeviceState.Temperature -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${state.current.toInt()}°C",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = iconColor
                        )
                        Text(
                            text = "Target: ${state.target.toInt()}°",
                            style = MaterialTheme.typography.bodySmall,
                            color = iconColor.copy(alpha = 0.7f)
                        )
                    }
                }
                is DeviceState.Media -> {
                    Column {
                        Slider(
                            value = state.volume.toFloat(),
                            onValueChange = { newValue ->
                                onValueChange(newValue)
                            },
                            valueRange = 0f..100f,
                            colors = SliderDefaults.colors(
                                thumbColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                activeTrackColor = iconColor,
                                inactiveTrackColor = iconColor.copy(alpha = 0.3f)
                            ),
                            modifier = Modifier.height(24.dp)
                        )
                        Text(
                            text = "${state.volume}%",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = iconColor,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
                else -> {
                    // For simple on/off devices
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isActive) "ON" else "OFF",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = iconColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Footer: Name & Status
            Column {
                Text(
                    text = device.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isActive)
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else
                        MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )
                Text(
                    text = getDeviceStatusText(device),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isActive)
                        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// Update the icon mapping function
//private fun getIconForDevice(type: DeviceType): ImageVector {
//    return when (type) {
//        DeviceType.LIGHT -> Icons.Rounded.Lightbulb
//        DeviceType.DIMMER -> Icons.Rounded.Lightbulb
//        DeviceType.RGB_LIGHT -> Icons.Rounded.Lightbulb
//        DeviceType.THERMOSTAT -> Icons.Rounded.Thermostat
//        DeviceType.HVAC -> Icons.Rounded.Air
//        DeviceType.FAN -> Icons.Rounded.Air
//        DeviceType.TV -> Icons.Rounded.Tv
//        DeviceType.AVR -> Icons.Rounded.SurroundSound
//        DeviceType.SOUNDBAR -> Icons.Rounded.Speaker
//        DeviceType.PROJECTOR -> Icons.Rounded.Videocam
//        DeviceType.MEDIA_PLAYER -> Icons.Rounded.PlayCircle
//        DeviceType.AUDIO_ZONE -> Icons.Rounded.MusicNote
//        DeviceType.SPEAKER -> Icons.Rounded.Speaker
//        DeviceType.DOOR_LOCK -> Icons.Rounded.Lock
//        DeviceType.GARAGE_DOOR -> Icons.Rounded.Garage
//        DeviceType.CAMERA -> Icons.Rounded.Videocam
//        DeviceType.MOTION_SENSOR -> Icons.Rounded.DirectionsRun
//        DeviceType.BLIND -> Icons.Rounded.Blinds
//        DeviceType.CURTAIN -> Icons.Rounded.Curtains
//        DeviceType.SMART_PLUG -> Icons.Rounded.Power
//        DeviceType.ENERGY_METER -> Icons.Rounded.ElectricBolt
//        DeviceType.UPS -> Icons.Rounded.BatteryFull
//        DeviceType.NETWORK_SWITCH -> Icons.Rounded.Lan
//        DeviceType.ACCESS_POINT -> Icons.Rounded.Wifi
//        DeviceType.CONTROLLER -> Icons.Rounded.Hub
//        DeviceType.SCENE -> Icons.Rounded.Dashboard
//        DeviceType.VIRTUAL_BUTTON -> Icons.Rounded.TouchApp
//        DeviceType.SYSTEM_MONITOR -> Icons.Rounded.MonitorHeart
//        else -> Icons.Rounded.Devices
//    }
//}

private fun getDeviceStatusText(device: SmartDevice): String {
    return when (val state = device.state) {
        is DeviceState.OnOff -> if (state.isOn) "On" else "Off"
        is DeviceState.Level -> "${state.value}%"
        is DeviceState.Temperature -> "${state.current.toInt()}°C"
        is DeviceState.Media -> "${state.volume}% • ${state.source}"
        is DeviceState.Position -> "${state.position}%"
        is DeviceState.Network -> if (state.online) "Online" else "Offline"
        else -> "Ready"
    }
}

@Preview
@Composable
fun DeviceCardPreview (){
    OrbitalTheme() {
        DeviceCard(
            SmartDevice(
                id = "light-1",
                name = "Test Light",
                type = DeviceType.DIMMER,
                state = DeviceState.OnOff(true),
                zoneId = "z1",
                metadata = DeviceMetadata(
                    "Test",
                    "Model",
                    "1.0",
                    isReachable = true,
                    lastSeenEpochMs = 0
                )
            ),
            {},
            {}
        )
    }

}