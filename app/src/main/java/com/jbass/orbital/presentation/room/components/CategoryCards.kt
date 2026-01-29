package com.jbass.orbital.presentation.room.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.jbass.orbital.domain.model.DeviceCategory
import com.jbass.orbital.ui.theme.OrbitalTheme

@Composable
fun CategoryCard(
    category: DeviceCategory,
    deviceCount: Int,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                //@Todo imageVector = category.icon,
                imageVector = Icons.Default.Home,
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            Text(category.name, style = MaterialTheme.typography.titleMedium)
            Text("$deviceCount devices", style = MaterialTheme.typography.labelMedium)
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewCategoryCard(){
    OrbitalTheme() {
        CategoryCard(
            DeviceCategory.LIGHTING,
            5,
            {}
        )
    }
}
