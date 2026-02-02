package com.jbass.orbital.presentation.dashboard.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jbass.orbital.domain.model.device.DeviceType
import com.jbass.orbital.presentation.util.getIcon
import com.jbass.orbital.ui.theme.OrbitalTheme

@Composable
fun StatusHeroItem(
    title: String,
    status: String,
    icon: ImageVector,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    val accentColor = MaterialTheme.colorScheme.primary

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .border(
                width = 1.dp,
                color = if (isActive) accentColor.copy(alpha = 0.3f) else Color.White.copy(alpha = 0.1f),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- ICON BOX ---
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    if (isActive) accentColor.copy(alpha = 0.1f) else Color.White.copy(alpha = 0.05f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isActive) accentColor else Color.White.copy(alpha = 0.4f),
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(20.dp))

        // --- TEXT CONTENT ---
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                color = if (isActive) accentColor else Color.White.copy(alpha = 0.4f)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = status,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Light
                ),
                color = Color.White
            )
        }

        // --- ACTIVE INDICATOR ---
        if (isActive) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(accentColor, CircleShape)
                    .shadow(elevation = 8.dp, shape = CircleShape, ambientColor = accentColor, spotColor = accentColor)
            )
        }
    }
}

@Preview(name = "System Status Gallery", backgroundColor = 0xFF0A0A0A, showBackground = true)
@Composable
fun PreviewStatusHero() {
    OrbitalTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0A0A0A))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatusHeroItem(
                title = "Security",
                status = "System Armed • All doors locked",
                icon = Icons.Rounded.Security,
                isActive = true
            )

            StatusHeroItem(
                title = "Climate",
                status = "Indoor average 22°C",
                icon = DeviceType.THERMOSTAT.getIcon(),
                isActive = false
            )

            StatusHeroItem(
                title = "Lighting",
                status = "3 zones active",
                icon = DeviceType.LIGHT.getIcon(),
                isActive = true
            )
        }
    }
}