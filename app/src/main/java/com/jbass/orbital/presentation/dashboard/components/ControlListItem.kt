package com.jbass.orbital.presentation.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jbass.orbital.domain.model.device.DeviceState
import com.jbass.orbital.domain.model.device.SmartDevice
import com.jbass.orbital.presentation.util.MockData
import com.jbass.orbital.presentation.util.getIcon
import com.jbass.orbital.ui.theme.OrbitalTheme


@Composable
fun ControlListItem(
    device: SmartDevice,
    onToggle: () -> Unit,
    onValueChange: (Float) -> Unit
) {
    val isOn = when (val s = device.state) {
        is DeviceState.OnOff -> s.isOn
        is DeviceState.Level -> s.value > 0
        is DeviceState.Media -> s.isOn
        is DeviceState.Position -> s.position > 0
        else -> false
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
            .clickable { onToggle() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon with active glow
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(
                    if (isOn) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    else Color.White.copy(alpha = 0.05f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = device.type.getIcon(),
                contentDescription = null,
                tint = if (isOn) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.4f),
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Label and Control
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = device.name,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Light
            )

            // If it's a dimmer, show the slider right below the name
            if (device.state is DeviceState.Level) {
                Spacer(modifier = Modifier.height(8.dp))
                Slider(
                    value = device.state.value.toFloat(),
                    onValueChange = onValueChange,
                    valueRange = 0f..100f,
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = Color.White.copy(alpha = 0.1f)
                    ),
                    modifier = Modifier.height(20.dp)
                )
            } else if (device.state is DeviceState.Position){
                Spacer(modifier = Modifier.height(8.dp))
                Slider(
                    value = device.state.position.toFloat(),
                    onValueChange = onValueChange,
                    valueRange = 0f..100f,
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = Color.White.copy(alpha = 0.1f)
                    ),
                    modifier = Modifier.height(20.dp)
                )
            }else {
                Text(
                    text = if (isOn) "ACTIVE" else "INACTIVE",
                    color = if (isOn) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.3f),
                    style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp)
                )
            }
        }
    }
}

@Preview()
@Composable
fun previewControlListItem(){
    OrbitalTheme() {
        Column(
            Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            MockData.Devices.forEach { device ->
                ControlListItem(
                    device  ,{},{}
                )

        }
        }
    }
}