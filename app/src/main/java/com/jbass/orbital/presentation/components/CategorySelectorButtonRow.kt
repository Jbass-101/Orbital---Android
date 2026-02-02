package com.jbass.orbital.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.jbass.orbital.domain.model.device.DeviceCategory
import com.jbass.orbital.ui.theme.OrbitalTheme


@Composable
fun RoomSelectorButtonRow(
    categories: List<DeviceCategory>,
    selectedCategory: DeviceCategory?,
    onFilter :(DeviceCategory?) -> Unit) {


    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        item {

            ElevatedButton(
                modifier = Modifier.clip(CircleShape),
                colors = ButtonDefaults.elevatedButtonColors(
                    contentColor = if (selectedCategory == null)
                        MaterialTheme.colorScheme.surface
                    else
                        MaterialTheme.colorScheme.onSurface,
                    containerColor = if (selectedCategory == null)
                        MaterialTheme.colorScheme.onSurface
                    else
                        MaterialTheme.colorScheme.surface

                ),
                onClick = {
                    onFilter(null)
                }
            ) {
                Text(
                    text = "All",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )

            }
        }
        items(categories) { category ->

            val isSelected = selectedCategory == category


            ElevatedButton(
                modifier = Modifier.clip(CircleShape),
                colors = ButtonDefaults.elevatedButtonColors(
                    contentColor = if (isSelected)
                        MaterialTheme.colorScheme.surface
                    else
                        MaterialTheme.colorScheme.onSurface,
                    containerColor = if (isSelected)
                        MaterialTheme.colorScheme.onSurface
                    else
                        MaterialTheme.colorScheme.surface

                ),
                onClick = { onFilter(category)}
            ) {
                Text(
                    text = category.name,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
                
            }
        }
    }
}


@PreviewLightDark
@Composable
fun PreviewRoomSelector (){
    OrbitalTheme() {
        RoomSelectorButtonRow(
        listOf(DeviceCategory.SECURITY, DeviceCategory.LIGHTING),
            null,
            {}
        )
    }
}