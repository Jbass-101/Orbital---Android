package com.jbass.orbital.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jbass.orbital.domain.model.*
import com.jbass.orbital.domain.repository.RealTimeClientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: RealTimeClientRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState = _uiState.asStateFlow()

    // One-time events (Snackbars, Navigation)
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        // 1. Connect on startup (using Emulator IP for now)
        connect("ws://192.168.0.152:9090//device")

        // 2. Combine Repository Flows into UI State
        viewModelScope.launch {
            combine(
                repository.deviceState,
                repository.connectionState
            ) { devices, connState ->
                DashboardUiState(
                    // Sort by Zone so the grid looks organized
                    devices = devices.sortedBy { it.zoneId },
                    connectionState = connState
                )
            }.collect { _uiState.value = it }
        }
        // 3. Listen for Backend Errors
        viewModelScope.launch {
            repository.uiErrors.collect { error ->
                val msg = when (error) {
                    is UiError.CommandRejected -> "Action Failed: ${error.message}"
                    is UiError.ConnectionError -> "Connection Lost"
                    is UiError.ParsingError -> "Data Error"
                }
                _uiEvent.send(UiEvent.ShowSnackbar(msg))
            }
        }
    }
    fun connect(url: String) = repository.connect(url)

    /**
     * Handles the complex logic of "Toggling" different device types.
     * It checks the current state and inverts it intelligently.
     */
    fun onToggleDevice(device: SmartDevice) {
        val newState = when (val s = device.state) {
            is DeviceState.OnOff -> s.copy(isOn = !s.isOn)
            is DeviceState.Media -> s.copy(isOn = !s.isOn)
            // For dimmers, "Toggle" usually means 0% -> 100% or 100% -> 0%
            is DeviceState.Level -> if (s.value > 0) DeviceState.Level(0) else DeviceState.Level(100)
            else -> return // Other types (like Thermostats) might not have a simple "Toggle"
        }

        sendCommand(device.id, newState, device.zoneId)
    }

    /**
     * Handles Sliders (Dimmers, Volume)
     */
    fun onLevelChange(device: SmartDevice, value: Float) {
        // Debounce logic could go here to prevent flooding the network
        val newState = when (device.state) {
            is DeviceState.Level -> DeviceState.Level(value.toInt())
            is DeviceState.Media -> device.state.copy(volume = value.toInt())
            else -> return
        }
        sendCommand(device.id, newState, device.zoneId)
    }

    private fun sendCommand(deviceId: String, newState: DeviceState, zoneId: String) {
        viewModelScope.launch {
            val command = ClientMessage.Command(
                requestId = UUID.randomUUID().toString(),
                deviceId = deviceId,
                newState = newState,
                zoneId = zoneId
            )
            repository.sendCommand(command)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            repository.disconnect()
        }
    }
}

//To Show SnackBar
sealed interface UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent
}