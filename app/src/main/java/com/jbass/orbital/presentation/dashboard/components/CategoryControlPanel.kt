package com.jbass.orbital.presentation.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import com.jbass.orbital.domain.model.device.DeviceCategory
import com.jbass.orbital.domain.model.device.SmartDevice
import com.jbass.orbital.presentation.util.MockData
import com.jbass.orbital.ui.theme.OrbitalTheme


@Composable
fun CategoryControlPanel(
    category: DeviceCategory,
    devices: List<SmartDevice>,
    onToggle: (SmartDevice) -> Unit,
    onValueChange: (SmartDevice, Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7f) // Occupies 70% of screen height
            .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
            .background(Color(0xFF0A0A0A).copy(alpha = 0.95f)) // Deep Glass
            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
            .padding(24.dp)
    ) {
        // Drag Handle
        Box(
            modifier = Modifier
                .size(40.dp, 4.dp)
                .background(Color.White.copy(alpha = 0.2f), CircleShape)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = category.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraLight,
            letterSpacing = 4.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(devices) { device ->
                // We use a simplified list-item version of your DeviceCard
                ControlListItem(
                    device = device,
                    onToggle = { onToggle(device) },
                    onValueChange = {onValueChange(device,it)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun previewCategoryControlPanel(){
    OrbitalTheme() {
        CategoryControlPanel(
            DeviceCategory.LIGHTING,
            MockData.Devices,
            {},
            { } as (SmartDevice, Float) -> Unit)
    }
}