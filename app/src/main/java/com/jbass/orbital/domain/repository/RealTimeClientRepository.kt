package com.jbass.orbital.domain.repository

import com.jbass.orbital.domain.model.ClientMessage
import com.jbass.orbital.domain.model.ConnectionState
import com.jbass.orbital.domain.model.SmartDevice
import com.jbass.orbital.domain.model.UiError
import com.jbass.orbital.domain.model.weather.CurrentWeather
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharedFlow

interface RealTimeClientRepository {

    val deviceState: StateFlow<List<SmartDevice>>

    val weatherState: StateFlow<CurrentWeather?>

    val connectionState: StateFlow<ConnectionState>

    val uiErrors: SharedFlow<UiError>

    fun connect(serverUrl: String)

    suspend fun sendCommand(command: ClientMessage)

    suspend fun disconnect()
}
