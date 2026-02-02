package com.jbass.orbital.presentation.util

import com.jbass.orbital.domain.model.ConnectionState
import com.jbass.orbital.domain.model.device.ClimateMode
import com.jbass.orbital.domain.model.device.DeviceMetadata
import com.jbass.orbital.domain.model.device.DeviceState
import com.jbass.orbital.domain.model.device.DeviceType
import com.jbass.orbital.domain.model.device.SmartDevice
import com.jbass.orbital.domain.model.weather.CurrentWeather
import com.jbass.orbital.domain.model.weather.WeatherCondition
import com.jbass.orbital.domain.model.weather.WeatherLocation
import com.jbass.orbital.presentation.dashboard.DashboardUiState

object MockData {
    val Metadata = DeviceMetadata(
        manufacturer = "Orbital Systems",
        model = "Pro-Series v2",
        firmwareVersion = "2.4.1",
        isReachable = true,
        lastSeenEpochMs = System.currentTimeMillis()
    )

    val Location = WeatherLocation(
        city = "Cape Town",
        country = "South Africa"
    )

    val Weather = CurrentWeather(
        temperature = 24.5,
        humidity = 42,
        pressure = 1012,
        windSpeed = 12.4,
        visibility = 10000,
        condition = WeatherCondition.CLEAR,
        location = Location
    )

    val Devices = listOf(
        // LIGHTING
        SmartDevice(
            id = "lt-01",
            name = "Main Chandelier",
            type = DeviceType.DIMMER,
            state = DeviceState.Level(75),
            zoneId = "Living Room",
            metadata = Metadata
        ),
        SmartDevice(
            id = "lt-02",
            name = "Kitchen Pendants",
            type = DeviceType.LIGHT,
            state = DeviceState.OnOff(false),
            zoneId = "Kitchen",
            metadata = Metadata
        ),

        // CLIMATE
        SmartDevice(
            id = "cl-01",
            name = "Main AC",
            type = DeviceType.THERMOSTAT,
            state = DeviceState.Temperature(current = 22f, target = 20f, mode = ClimateMode.HEAT),
            zoneId = "Living Room",
            metadata = Metadata
        ),

        // MEDIA
        SmartDevice(
            id = "av-01",
            name = "Home Theater TV",
            type = DeviceType.TV,
            state = DeviceState.Media(isOn = true, volume = 30, source = "Netflix"),
            zoneId = "Cinema",
            metadata = Metadata
        ),

        // SHADING
        SmartDevice(
            id = "sh-01",
            name = "Patio Blinds",
            type = DeviceType.BLIND,
            state = DeviceState.Level(0), // Closed
            zoneId = "Patio",
            metadata = Metadata
        ),

        // SECURITY
        SmartDevice(
            id = "sec-01",
            name = "Front Door",
            type = DeviceType.DOOR_LOCK,
            state = DeviceState.OnOff(true), // Locked
            zoneId = "Entry",
            metadata = Metadata
        )
    )

    val DashboardState = DashboardUiState(
        devices = Devices,
        weather = Weather,
        isLoading = false,
        connectionState = ConnectionState.Connected,
        selectedRoomId = "Living Room",
        selectedCategory = null
    )
}