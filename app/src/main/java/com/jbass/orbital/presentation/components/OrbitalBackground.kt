package com.jbass.orbital.presentation.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jbass.orbital.R
import com.jbass.orbital.ui.theme.OrbitalTheme

@Composable
fun OrbitalBackground(
    bgRes : Int = R.drawable.dashboard_bg,
    blurRadius: Dp = 12.dp,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,

    ) {
    
    val animatedBlur by animateDpAsState(
        targetValue = blurRadius,
        animationSpec = tween(durationMillis = 600, easing = LinearOutSlowInEasing),
        label = "blurAnimation"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        Image(
            painter = painterResource(bgRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
                .blur(animatedBlur)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to Color.Black.copy(alpha = 0.6f),
                        0.4f to Color.Transparent,
                        1f to Color.Black.copy(alpha = 0.8f)
                    )
                )
        )
        content()
    }
}

@Preview
@Composable
fun previewBackground(){
    OrbitalTheme {
        OrbitalBackground{ }
    }
}