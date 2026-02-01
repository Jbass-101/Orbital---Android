package com.jbass.orbital.presentation.dashboard.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jbass.orbital.R
import com.jbass.orbital.ui.theme.OrbitalTheme

@Composable
fun DashboardBackground(
    modifier: Modifier = Modifier,
    bgRes : Int = R.drawable.house_bg,
    content: @Composable BoxScope.() -> Unit,

) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A))
    ) {

        Image(
            painter = painterResource(bgRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
                .blur(10.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to Color.Black.copy(alpha = 0.6f),
                        0.5f to Color.Black.copy(alpha = 0.7f),
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
    OrbitalTheme() {
        DashboardBackground(
        ) { }
    }
}