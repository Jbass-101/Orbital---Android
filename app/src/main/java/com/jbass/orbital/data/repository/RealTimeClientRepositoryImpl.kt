package com.jbass.orbital.data.repository

import com.jbass.orbital.data.remote.RealTimeClient
import com.jbass.orbital.domain.model.message.ClientMessage
import com.jbass.orbital.domain.model.ConnectionState
import com.jbass.orbital.domain.model.device.SmartDevice
import com.jbass.orbital.domain.model.UiError
import com.jbass.orbital.domain.model.zone.Zone
import com.jbass.orbital.domain.model.weather.CurrentWeather
import com.jbass.orbital.domain.repository.RealTimeClientRepository
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

class RealTimeRepositoryImpl(
    private val client: RealTimeClient
) : RealTimeClientRepository {

    override val deviceState: StateFlow<List<SmartDevice>> =
        client.deviceState

    override val weatherState: StateFlow<CurrentWeather?> =
        client.weatherState

    override val zoneState: StateFlow<List<Zone>> =
        client.zoneState

    override val connectionState: StateFlow<ConnectionState> =
        client.connectionStatus

    override val uiErrors: SharedFlow<UiError> =
        client.uiErrors

    override fun connect(serverUrl: String) {
        client.connect(serverUrl)
    }

    override suspend fun sendCommand(command: ClientMessage) {
        client.sendCommand(command)
    }

    override suspend fun disconnect() {
        client.close()
    }
}
