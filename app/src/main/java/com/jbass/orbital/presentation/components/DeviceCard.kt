package com.jbass.orbital.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.VolumeDown
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.VolumeDown
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jbass.orbital.R
import com.jbass.orbital.domain.model.device.DeviceCategory
import com.jbass.orbital.domain.model.device.DeviceMetadata
import com.jbass.orbital.domain.model.device.DeviceState
import com.jbass.orbital.domain.model.device.DeviceType
import com.jbass.orbital.domain.model.device.SmartDevice
import com.jbass.orbital.presentation.util.MockData
import com.jbass.orbital.presentation.util.getIcon
import com.jbass.orbital.ui.theme.OrbitalTheme

@Composable
fun DeviceCard(
    device: SmartDevice,
    onToggle: () -> Unit,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val isMedia = device.type.category == DeviceCategory.MEDIA
    val isOn = when (val s = device.state) {
        is DeviceState.OnOff -> s.isOn
        is DeviceState.Level -> s.value > 0
        is DeviceState.Media -> s.isOn
        else -> false
    }

    val accentColor = MaterialTheme.colorScheme.primary
    val glassAlpha = if (isOn) 0.15f else 0.08f

    // We remove aspectRatio(1f) from the internal modifier so the parent Grid can decide the shape
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(32.dp))
            .background(Color.White.copy(alpha = glassAlpha))
            .border(
                1.dp,
                if (isOn) accentColor.copy(alpha = 0.4f) else Color.White.copy(alpha = 0.12f),
                RoundedCornerShape(32.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onToggle() }
            .padding(20.dp)
    ) {
        if (isMedia && device.state is DeviceState.Media) {
            MediaHeroContent(device.name, device.state, onValueChange, accentColor)
        } else {
            StandardTileContent(device, isOn, onValueChange, accentColor)
        }
    }
}

@Composable
private fun MediaHeroContent(
    name: String,
    state: DeviceState.Media,
    onVolumeChange: (Float) -> Unit,
    accentColor: Color
) {
    Column(verticalArrangement = Arrangement.SpaceBetween) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(42.dp).background(Color.White.copy(alpha = 0.05f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (state.isOn) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                    contentDescription = null,
                    tint = if (state.isOn) accentColor else Color.White.copy(alpha = 0.6f)
                )
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text(name, color = Color.White, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = if (state.isOn) "PLAYING: ${state.source}" else "STANDBY",
                    color = Color.White.copy(alpha = 0.5f),
                    style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp)
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // Volume Slider
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.VolumeDown, null, tint = Color.White.copy(alpha = 0.4f), modifier = Modifier.size(16.dp))
            Slider(
                value = state.volume.toFloat(),
                onValueChange = onVolumeChange,
                valueRange = 0f..100f,
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                colors = SliderDefaults.colors(activeTrackColor = accentColor, thumbColor = Color.White)
            )
            Icon(Icons.Rounded.VolumeUp, null, tint = Color.White.copy(alpha = 0.4f), modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
private fun StandardTileContent(
    device: SmartDevice,
    isOn: Boolean,
    onValueChange: (Float) -> Unit,
    accentColor: Color
) {
    Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.heightIn(min = 140.dp)) {
        Icon(
            imageVector = device.type.getIcon(),
            contentDescription = null,
            tint = if (isOn) accentColor else Color.White.copy(alpha = 0.6f),
            modifier = Modifier.size(28.dp)
        )

        Column {
            when (val state = device.state) {
                is DeviceState.Temperature -> {
                    Text("${state.current}°", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.ExtraLight, color = Color.White)
                }
                is DeviceState.Level -> {
                    Slider(
                        value = state.value.toFloat(),
                        onValueChange = onValueChange,
                        colors = SliderDefaults.colors(activeTrackColor = accentColor, thumbColor = Color.White)
                    )
                }

                else -> {}
            }
            Text(device.name, color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
            Text(getStatusText(device).uppercase(), color = Color.White.copy(alpha = 0.5f), style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp))
        }
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

@Preview(name = "Light Tile", backgroundColor = 0xFF0A0A0A, showBackground = true)
@Composable
fun PreviewSavantLight() {
    OrbitalTheme(darkTheme = true) {
        Column(Modifier
            .fillMaxSize()) {
            MockData.Devices.forEach { device ->
                DeviceCard(
                    device = device,
                    onToggle = {},
                    onValueChange = {},
                )
            }
        }
    }
}
private fun mockDevice(type: DeviceType, state: DeviceState, name: String) = SmartDevice(
    id = "id", name = name, type = type, state = state, zoneId = "z1",
    metadata = DeviceMetadata(
        "T", "M", "1", "192.168.0.1", "00:00:",
        isReachable = true,
        lastSeenEpochMs = 56000
    )
)