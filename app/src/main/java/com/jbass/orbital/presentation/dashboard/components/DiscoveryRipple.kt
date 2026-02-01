package com.jbass.orbital.presentation.dashboard.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jbass.orbital.ui.theme.OrbitalTheme

@Composable
fun DiscoveryRipple(
    statusText: String = "Searching for Orbital Hub...",
    showManualInput : () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "DiscoveryRipple")

    val waves = listOf(0, 700, 1400)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color.Black,
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        waves.forEach { delay ->
            val progress by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2100, delayMillis = delay, easing = LinearOutSlowInEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "waveProgress"
            )

            // Expanding circle
            Box(
                Modifier
                    .size(250.dp)
                    .scale(progress)
                    .alpha(1f - progress)
                    .border(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), CircleShape)
            )
        }

        // Central "Hub" Node
        Box(
            Modifier
                .size(12.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape)
        )

        // Status Text at bottom
        Text(
            text = statusText.uppercase(),
            style = MaterialTheme.typography.labelLarge.copy(
                letterSpacing = 4.sp,
                fontWeight = FontWeight.Light
            ),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 120.dp)
        )

        TextButton(
            onClick =  showManualInput,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 120.dp)
        ){
            Text(
                text = "Enter address manually",
                style = MaterialTheme.typography.labelLarge.copy(
                    letterSpacing = 4.sp,
                ) )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDiscoveryRipple(){
    OrbitalTheme {
        DiscoveryRipple(
            "Searching for Orbital hub",
            {}
        )
    }
}