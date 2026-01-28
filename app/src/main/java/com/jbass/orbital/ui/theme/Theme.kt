package com.jbass.orbital.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


val DarkColorScheme = darkColorScheme(
    surfaceContainer = Color(0x80464646),
    background = Color(0xFF000000),
    surface = Color(0xFF181820),
    surfaceVariant = Color(0xFF1A1A1A),

    primary = Color(0xFFFFFFFF),
    onPrimary = Color(0xFF000000),

    onBackground = Color(0xFFFFFFFF),
    onSurface = Color(0xFFFFFFFF),

    outline = Color(0xFF262626),
    outlineVariant = Color(0xFF1A1A1A)
)


val LightColorScheme = lightColorScheme(
    surfaceContainer = Color(0xFFFFFFFF),
    background = Color(0xFFE3E3E3),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFEEEEEE),

    primary = Color(0xFF000000),
    onPrimary = Color(0xFFFFFFFF),

    onBackground = Color(0xFF000000),
    onSurface = Color(0xFF000000),

    outline = Color(0xFFE0E0E0),
    outlineVariant = Color(0xFFEEEEEE)
)


@Composable
fun OrbitalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}