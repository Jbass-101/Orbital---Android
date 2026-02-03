package com.jbass.orbital.presentation.dashboard.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.AutoAwesomeMosaic
import androidx.compose.material.icons.rounded.Blinds
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.Router
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.Thermostat
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jbass.orbital.domain.model.device.DeviceCategory
import com.jbass.orbital.ui.theme.OrbitalTheme

@Composable
fun BottomNavBar(
    categories: List<DeviceCategory>,
    selectedCategory: DeviceCategory?,
    showAllOption: Boolean = true,
    onFilter :(DeviceCategory?) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 24.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(Color.White.copy(alpha = 0.08f))
            .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(32.dp))
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(showAllOption){

                item {
                    BottomNavItem(
                        icon = Icons.Rounded.AutoAwesomeMosaic,
                        label = "All",
                        selected = selectedCategory == null,
                        onClick = { onFilter(null) }
                    )
                }
            }
            items(categories) { category ->

                BottomNavItem(
                    icon = getIconForCategory(category),
                    label = category.name.lowercase().replaceFirstChar { it.uppercase() },
                    selected = selectedCategory == category,
                    onClick = { onFilter(category) }
                )
            }
        }
    }
}

@Composable
fun BottomNavItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val accentColor = MaterialTheme.colorScheme.primary
    val inactiveColor = Color.White.copy(alpha = 0.5f)
    val contentColor = if (selected) accentColor else inactiveColor

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
            .padding(horizontal = 12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = contentColor,
            modifier = Modifier.size(32.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = label,
            color = contentColor,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                letterSpacing = 0.5.sp
            )
        )
        // Subtle indicator dot
        Box(
            modifier = Modifier
                .padding(top = 4.dp)
                .size(4.dp)
                .background(
                    if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
                    CircleShape
                )
        )
    }
}

private fun getIconForCategory(category: DeviceCategory): ImageVector {
    return when (category) {
        DeviceCategory.LIGHTING -> Icons.Rounded.Lightbulb
        DeviceCategory.AUDIO -> Icons.Rounded.MusicNote
        DeviceCategory.MEDIA -> Icons.Rounded.PlayCircle
        DeviceCategory.CLIMATE -> Icons.Rounded.Thermostat
        DeviceCategory.SHADING -> Icons.Rounded.Blinds
        DeviceCategory.SECURITY -> Icons.Rounded.Security
        DeviceCategory.ENERGY -> Icons.Rounded.Bolt
        DeviceCategory.NETWORK -> Icons.Rounded.Router
        DeviceCategory.VIRTUAL -> Icons.Rounded.AutoAwesome
    }
}

@Preview(name = "Orbital Dark Nav", group = "Navigation", backgroundColor = 0xFF0A0A0A, showBackground = true)
@Composable
fun PreviewBottomNavFull() {
    OrbitalTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0A0A0A))
        ) {

            BottomNavBar(
                categories = DeviceCategory.entries,
                selectedCategory = null,
                onFilter = {}
            )
            DeviceCategory.entries.forEach { category ->

                BottomNavBar(
                    categories = DeviceCategory.entries,
                    selectedCategory = category,
                    onFilter = {}
                )
            }
        }
    }
}