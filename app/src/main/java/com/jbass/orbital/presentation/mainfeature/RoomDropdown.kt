package com.jbass.orbital.presentation.mainfeature

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.jbass.orbital.domain.model.Zone
import com.jbass.orbital.ui.theme.OrbitalTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomDropdown(
    rooms: List<Zone>,
    selectedRoomId: String?,
    onRoomSelected: (String?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedRoom = rooms.find { it.id == selectedRoomId }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedRoom?.name ?: "Select Room",
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            rooms.forEach { room ->
                DropdownMenuItem(
                    text = { Text(room.name) },
                    onClick = {
                        expanded = false
                        onRoomSelected(room.id)
                    }
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun previewRoomDropdown(){
    OrbitalTheme() {
        RoomDropdown(
            listOf(
                Zone("a","Bedroom"),
                Zone("b","Kitchen"),),
            "a",
            {}
        )
    }
}
