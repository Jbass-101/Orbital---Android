package com.jbass.orbital.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.jbass.orbital.domain.model.device.DeviceMetadata
import com.jbass.orbital.domain.model.device.DeviceState
import com.jbass.orbital.domain.model.device.DeviceType
import com.jbass.orbital.domain.model.device.SmartDevice
import com.jbass.orbital.presentation.util.getIcon
import com.jbass.orbital.ui.theme.OrbitalTheme

@Composable
fun DeviceCard(
    device: SmartDevice,
    onToggle: () -> Unit,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val isOn = when (val s = device.state) {
        is DeviceState.OnOff -> s.isOn
        is DeviceState.Level -> s.value > 0
        is DeviceState.Media -> s.isOn
        else -> false
    }

    // Luxury Theme Constants
    val accentColor = MaterialTheme.colorScheme.primary
    val glassAlpha = if (isOn) 0.15f else 0.08f
    val borderAlpha = if (isOn) 0.4f else 0.12f

    val animatedBorderColor by animateColorAsState(
        targetValue = if (isOn) accentColor.copy(alpha = borderAlpha) else Color.White.copy(alpha = borderAlpha),
        label = "borderGlow"
    )

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(32.dp)) // Savant-style high radius
            .background(Color.White.copy(alpha = glassAlpha))
            .border(1.dp, animatedBorderColor, RoundedCornerShape(32.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null // Removes cheap ripple for premium feel
            ) { onToggle() }
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // ─── Header: Icon ───────────────────────────────
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.05f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = device.type.getIcon(),
                    contentDescription = null,
                    tint = if (isOn) accentColor else Color.White.copy(alpha = 0.6f),
                    modifier = Modifier.size(24.dp)
                )
            }

            // ─── Middle: Specific Controls ───────────────────────
            Box(modifier = Modifier.fillMaxWidth()) {
                when (val state = device.state) {
                    is DeviceState.Level -> {
                        Slider(
                            value = state.value.toFloat(),
                            onValueChange = onValueChange,
                            valueRange = 0f..100f,
                            colors = SliderDefaults.colors(
                                thumbColor = Color.White,
                                activeTrackColor = accentColor,
                                inactiveTrackColor = Color.White.copy(alpha = 0.1f)
                            ),
                            modifier = Modifier.height(24.dp)
                        )
                    }

                    is DeviceState.Temperature -> {
                        Text(
                            text = "${state.current}°",
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.ExtraLight, // Expensive look
                            color = Color.White
                        )
                    }

                    else -> {

                    }
                }
            }

            // ─── Footer: Labels ───────────────────────────────
            Column {
                Text(
                    text = device.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    maxLines = 1
                )
                Text(
                    text = getStatusText(device).uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 1.2.sp,
                        fontWeight = FontWeight.Light
                    ),
                    color = Color.White.copy(alpha = 0.5f)
                )
            }
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
        Box(Modifier.padding(20.dp).size(180.dp)) {
            DeviceCard(
                device = mockDevice(DeviceType.LIGHT, DeviceState.OnOff(true), "Kitchen Light"),
                onToggle = {},
                onValueChange = {}
            )
        }
    }
}

@Preview(name = "Dimmer Tile", backgroundColor = 0xFF0A0A0A, showBackground = true)
@Composable
fun PreviewSavantDimmer() {
    OrbitalTheme(darkTheme = true) {
        Box(Modifier.padding(20.dp).size(180.dp)) {
            DeviceCard(
                device = mockDevice(DeviceType.DIMMER, DeviceState.Level(65), "Chandelier"),
                onToggle = {},
                onValueChange = {}
            )
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