package com.jbass.orbital.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Dashboard : Screen(
        route = "home",
        label = "Home",
        icon = Icons.Default.Home
    )


    object Rooms : Screen("rooms", "Rooms", Icons.Default.Home)

    data object CategoryDevices : Screen(
        route = "c/{roomId}/{category}",
        label = "Home",
        icon = Icons.Default.Home
    ) {
        fun createRoute(roomId: String, category: String): String =
            "category_devices/$roomId/$category"
    }
}
