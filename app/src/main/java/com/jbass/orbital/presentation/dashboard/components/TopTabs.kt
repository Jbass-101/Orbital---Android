package com.jbass.orbital.presentation.dashboard.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jbass.orbital.ui.theme.OrbitalTheme

@Composable
fun TopTabs(
    modifier: Modifier = Modifier,
    onRoomsClick: () -> Unit = {},
    onScenesClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TopTabItem("SCENES", onClick = onScenesClick)
        TopTabItem("ROOMS", onClick = onRoomsClick)
        TopTabItem("SETTINGS", onClick = onSettingsClick)
    }
}

@Composable
fun TopTabItem(
    title: String,
    onClick: () -> Unit
) {
    Text(
        text = title,
        color = Color.White.copy(alpha = 0.7f), // Muted for luxury feel
        style = MaterialTheme.typography.labelLarge.copy(
            letterSpacing = 2.sp, // Spread out letters look more premium
            fontWeight = FontWeight.Light
        ),
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 4.dp)
    )
}

@PreviewLightDark
@Composable
fun PreviewTopTabs (){
    OrbitalTheme() {
        TopTabs()
    }
}
