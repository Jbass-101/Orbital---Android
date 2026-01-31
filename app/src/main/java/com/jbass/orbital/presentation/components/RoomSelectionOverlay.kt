package com.jbass.orbital.presentation.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jbass.orbital.R
import com.jbass.orbital.domain.model.Zone
import com.jbass.orbital.ui.theme.OrbitalTheme

@Composable
fun RoomSelectionOverlay(
    rooms: List<Zone>, // Replace with Room model if you have one
    onRoomSelected: (String) -> Unit,
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.9f))
            .clickable { onClose() } // Close when tapping background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Header for Overlay
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SELECT ROOM",
                    style = MaterialTheme.typography.labelLarge.copy(
                        letterSpacing = 4.sp,
                        fontWeight = FontWeight.Light,
                        color = Color.White
                    )
                )
                // Close button could go here or just tap background
            }

            LazyColumn(
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(rooms) { room ->
                    RoomCard(
                        name = room.name,
                        onClick = {
                            onRoomSelected(room.id)
                            onClose()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RoomCard(name: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.1f))
            .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(24.dp))
            .clickable { onClick() }
    ) {

        // --- BACKGROUND LAYER ---
        Image(
            painter = painterResource(id = R.drawable.house_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
                .blur(0.dp) // Increased blur for better readability
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)))
        Text(
            text = name.uppercase(),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 24.dp),
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.ExtraLight,
                letterSpacing = 2.sp,
                color = Color.White
            )
        )
    }
}

@Preview
@Composable
fun previewRoomSelection(){
    OrbitalTheme() {
        RoomSelectionOverlay(
            rooms = listOf(
                Zone("1","Bedroom"),
                Zone("1","Bedroom"),
                Zone("1","Bedroom"),
                Zone("1","Bedroom"),
                Zone("1","Bedroom")
            ),
            {},
            {}
        )
    }
}