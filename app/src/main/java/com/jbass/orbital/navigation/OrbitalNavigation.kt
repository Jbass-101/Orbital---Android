package com.jbass.orbital.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jbass.orbital.domain.model.ConnectionState
import com.jbass.orbital.presentation.dashboard.DashboardContent
import com.jbass.orbital.presentation.dashboard.DashboardViewModel
import com.jbass.orbital.presentation.loading.DiscoveryRipple
import com.jbass.orbital.presentation.room.RoomDetailScreen

@Composable
fun OrbitalNavigation(
    viewModel: DashboardViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()

    val state by viewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.Loading.route,
    ) {
        // --- 1. DISCOVERY / LOADING SCREEN ---
        composable(Screen.Loading.route) {
            // Navigation Effect: Jump to Dashboard when connected
            LaunchedEffect(state.connectionState) {
                if (state.connectionState is ConnectionState.Connected) {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Loading.route) { inclusive = true }
                    }
                }
            }

            DiscoveryRipple(
                statusText = when (state.connectionState) {
                    is ConnectionState.Reconnecting -> "Reconnecting..."
                    else -> "Scanning Local Network..."
                },
                showManualInput = { viewModel.showManualInput() }
            )
        }


        // --- DASHBOARD DESTINATION ---
        composable(
            Screen.Dashboard.route,enterTransition = {
                fadeIn(animationSpec = tween(500)) +
                        scaleIn(initialScale = 1.1f, animationSpec = tween(500))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(500)) +
                        scaleOut(targetScale = 0.9f, animationSpec = tween(500))
            }
            ) {
            DashboardContent(
                state = state,
                onFilter = viewModel::onFilter,
                onToggleDevice = viewModel::onToggleDevice,
                onLevelChange = viewModel::onLevelChange,
                onRoomSelected = { roomId ->
                    // Navigate to room detail when a room is picked
                    navController.navigate(Screen.RoomDetail.createRoute(roomId))
                },
                onCloseCategory = viewModel::onCloseCategoryControl
            )
        }

        // --- ROOM DETAIL DESTINATION ---
        composable(
            route = Screen.RoomDetail.route,
            arguments = listOf(navArgument("roomId") { type = NavType.StringType }),
            enterTransition = {
                fadeIn(animationSpec = tween(500)) +
                        scaleIn(initialScale = 0.9f, animationSpec = tween(500))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(500)) +
                        scaleOut(targetScale = 1.1f, animationSpec = tween(500))
            }
        ) { backStackEntry ->
            val roomId = backStackEntry.arguments?.getString("roomId")
            val selectedRoom = state.rooms.find { it.id == roomId }

            RoomDetailScreen(
                roomName = selectedRoom?.name ?: "Room",
                // Filter devices for this specific room
                devices = state.devices.filter { it.zoneId == roomId },
                onBackClick = { navController.popBackStack() },
                onToggleDevice = viewModel::onToggleDevice,
                onLevelChange = viewModel::onLevelChange
            )
        }
    }
}
