package com.jbass.orbital.presentation.components


import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jbass.orbital.R
import com.jbass.orbital.domain.model.DeviceMetadata
import com.jbass.orbital.domain.model.DeviceState
import com.jbass.orbital.domain.model.DeviceType
import com.jbass.orbital.domain.model.SmartDevice
import com.jbass.orbital.ui.theme.OrbitalTheme

@Composable
fun DeviceCard(
    device: SmartDevice,
    onToggle: () -> Unit,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    // 1. Determine "Active" state for coloring
    val isActive = when (val s = device.state) {
        is DeviceState.OnOff -> s.isOn
        is DeviceState.Level -> s.value > 0
        is DeviceState.Media -> s.isOn
        else -> false
    }

    // 2. Animations
    val cardColor by animateColorAsState(
        targetValue = if (isActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
        label = "cardBg"
    )
    val iconColor by animateColorAsState(
        targetValue = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
        label = "iconTint"
    )

    Column(
        modifier = modifier
            .aspectRatio(1f) // Square Shape
            .clip(RoundedCornerShape(24.dp))
            .background(cardColor)
            .clickable { onToggle() }
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // --- Header: Icon ---
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Color.White.copy(alpha = 0.5f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(getIconForDevice(device.type)),
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }

        // --- Body: Controls (Polymorphic) ---
        // Only show Sliders for Dimmers/Media
        when (val state = device.state) {
            is DeviceState.Level -> {
                Slider(
                    value = state.value.toFloat(),
                    onValueChange = onValueChange,
                    valueRange = 0f..100f,
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = iconColor
                    ),
                    modifier = Modifier.height(20.dp)
                )
            }
            is DeviceState.Temperature -> {
                Text(
                    text = "${state.current}°C",
                    style = MaterialTheme.typography.headlineSmall,
                    color = iconColor
                )
            }
            else -> Spacer(Modifier.height(8.dp))
        }

        // --- Footer: Name & Status ---
        Column {
            Text(
                text = device.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = if (isActive) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
            Text(
                text = getStatusText(device),
                style = MaterialTheme.typography.labelSmall,
                color = if (isActive) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

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

@Preview(showBackground = true)
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