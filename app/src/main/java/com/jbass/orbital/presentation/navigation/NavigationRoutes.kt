package com.jbass.orbital.presentation.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object RoomDetail : Screen("room_detail/{roomId}") {
        fun createRoute(roomId: String) = "room_detail/$roomId"
    }
}