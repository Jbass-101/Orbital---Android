package com.jbass.orbital.presentation.room


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jbass.orbital.R
import com.jbass.orbital.domain.model.device.DeviceCategory
import com.jbass.orbital.domain.model.device.SmartDevice
import com.jbass.orbital.presentation.components.DeviceCard
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
    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {

        // 1. Background - Crisp or Light Blur (2dp) to differentiate from Dashboard
        Image(
            painter = painterResource(roomImageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().blur(2.dp)
        )

        // Gradient for readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Black.copy(alpha = 0.6f), Color.Transparent, Color.Black.copy(alpha = 0.8f))
                    )
                )
        )

        // 2. The Content
        Column(modifier = Modifier.fillMaxSize()) {

            // Header with Back Button
            RoomDetailHeader(
                roomName = roomName,
                onBackClick = onBackClick
            )

            // High-Density Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 40.dp, top = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = devices,
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
                        modifier = Modifier.fillMaxWidth()
                    )
                }
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