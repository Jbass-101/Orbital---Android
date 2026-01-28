package com.jbass.orbital.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.jbass.orbital.ui.theme.OrbitalTheme
import androidx.compose.ui.graphics.Color


@Composable
fun RoomSelectorButtonRow(rooms: List<String>) {
    var selectedRoom by remember { mutableStateOf(rooms.first()) }

    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(rooms) { room ->
            val isSelected = room == selectedRoom

            ElevatedButton(
                modifier = Modifier.clip(CircleShape),
                colors = ButtonDefaults.elevatedButtonColors(
                    contentColor = Color.Red,
                    containerColor = MaterialTheme.colorScheme.surface

                ),
                onClick = { selectedRoom = room }
            ) {
                Text(
                    text = room,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
                
            }

//            Surface(
//                modifier = Modifier.clip(CircleShape),
//                color = if (isSelected)
//                    MaterialTheme.colorScheme.onBackground
//                else
//                    MaterialTheme.colorScheme.surface,
//                onClick = { selectedRoom = room }
//            ) {
//                Text(
//                    text = room,
//                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
//                    color = if (isSelected)
//                        MaterialTheme.colorScheme.background
//                    else
//                        MaterialTheme.colorScheme.onSurface
//                )
//            }
        }
    }
}


@PreviewLightDark
@Composable
fun PreviewRoomSelector (){
    OrbitalTheme() {
        RoomSelectorButtonRow(
        listOf("Bedroom","Kitchen", "Braai Area")
        )
    }
}