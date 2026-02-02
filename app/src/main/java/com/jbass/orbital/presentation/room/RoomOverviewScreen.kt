package com.jbass.orbital.presentation.room


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jbass.orbital.domain.model.device.DeviceCategory
import com.jbass.orbital.presentation.dashboard.DashboardUiState
import com.jbass.orbital.presentation.room.components.CategoryCard
import com.jbass.orbital.presentation.room.components.RoomDropdown

@Composable
fun RoomOverviewScreen(
    state: DashboardUiState,
    onRoomSelected: (String) -> Unit,
    onCategorySelected: (DeviceCategory) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        RoomDropdown(
            rooms = state.rooms,
            selectedRoomId = state.selectedRoomId,
            onRoomSelected = { onRoomSelected(it!!)}
        )

        Spacer(Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Adaptive(160.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DeviceCategory.entries.forEach { category ->
                val count = state.devices.count {
                    it.zoneId == state.selectedRoomId &&
                            it.type.category == category
                }

                if (count > 0) {
                    item {
                        CategoryCard(
                            category = category,
                            deviceCount = count,
                            onClick = { onCategorySelected(category) }
                        )
                    }
                }
            }
        }
    }
}
