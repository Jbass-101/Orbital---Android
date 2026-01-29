package com.jbass.orbital.presentation.dashboard

import com.jbass.orbital.domain.model.ConnectionState
import com.jbass.orbital.domain.model.DeviceCategory
import com.jbass.orbital.domain.model.SmartDevice
import com.jbass.orbital.domain.model.Zone
import com.jbass.orbital.domain.model.weather.CurrentWeather


data class DashboardUiState(
    val devices: List<SmartDevice> = emptyList(),
    val rooms: List<Zone> = emptyList(),
    val selectedRoomId: String? = null,
    val filteredDevices: List<SmartDevice> = emptyList(),
    val weather: CurrentWeather? = null,
    val selectedCategory: DeviceCategory? = null,
    val connectionState: ConnectionState = ConnectionState.Disconnected,
    val isLoading: Boolean = true
)