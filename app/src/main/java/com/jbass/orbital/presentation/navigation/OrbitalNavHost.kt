package com.jbass.orbital.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jbass.orbital.domain.model.DeviceCategory
import com.jbass.orbital.presentation.dashboard.DashboardScreen
import com.jbass.orbital.presentation.dashboard.DashboardViewModel
import com.jbass.orbital.presentation.room.RoomCategoryDevicesScreen
import com.jbass.orbital.presentation.room.RoomOverviewScreen

@Composable
fun OrbitalNavHost(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
        modifier = modifier
    ) {

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onRoomsClick = {
                    navController.navigate(Screen.Rooms.route)
                }
            )
        }


        //We are sharing the viewmodel
        composable(Screen.Rooms.route) {
            RoomOverviewRoute(navController)
        }



        composable(
            Screen.CategoryDevices.route,
            arguments = listOf(
                navArgument("roomId") { type = NavType.StringType },
                navArgument("category") { type = NavType.StringType }
            )
        ) {
            CategoryDevicesRoute(navController)
        }

    }
}

@Composable
fun RoomOverviewRoute(
    navController: NavController
) {
    val parentEntry = remember(navController.currentBackStackEntry) {
        navController.getBackStackEntry(Screen.Rooms.route)
    }

    val viewModel: DashboardViewModel = hiltViewModel(parentEntry)
    val state by viewModel.uiState.collectAsState()

    RoomOverviewScreen(
        state = state,
        onRoomSelected = viewModel::onRoomSelected,
        onCategorySelected = { category ->
            navController.navigate(
                Screen.CategoryDevices.createRoute(
                    state.selectedRoomId!!,
                    category.name
                )
            )
        }
    )
}

@Composable
fun CategoryDevicesRoute(
    navController: NavController
) {
    val parentEntry = remember(navController.currentBackStackEntry) {
        navController.getBackStackEntry(Screen.Rooms.route)
    }

    val viewModel: DashboardViewModel = hiltViewModel(parentEntry)
    val state by viewModel.uiState.collectAsState()

    val entry = navController.currentBackStackEntry!!
    val roomId = entry.arguments!!.getString("roomId")!!
    val category = DeviceCategory.valueOf(
        entry.arguments!!.getString("category")!!
    )

    RoomCategoryDevicesScreen(
        roomName = state.rooms.first { it.id == roomId }.name,
        category = category,
        devices = state.devices.filter {
            it.zoneId == roomId && it.type.category == category
        },
        onToggle = viewModel::onToggleDevice,
        onLevelChange = viewModel::onLevelChange
    )
}


