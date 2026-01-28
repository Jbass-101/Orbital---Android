package com.jbass.orbital.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector

sealed class DashboardNavigation(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Home : DashboardNavigation(
        route = "home",
        label = "Home",
        icon = Icons.Default.Home
    )

    object Devices : DashboardNavigation(
        route = "devices",
        label = "Devices",
        icon = Icons.Default.Delete
    )

    object Scenes : DashboardNavigation(
        route = "scenes",
        label = "Scenes",
        icon = Icons.Default.Warning
    )

    object Profile : DashboardNavigation(
        route = "profile",
        label = "Profile",
        icon = Icons.Default.Person
    )
}
