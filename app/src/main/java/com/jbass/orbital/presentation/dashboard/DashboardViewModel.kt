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

data class DashboardUiState(
    val devices: List<SmartDevice> = emptyList(),
    val connectionState: ConnectionState = ConnectionState.Disconnected,
    val isLoading: Boolean = true
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: RealTimeClientRepository
) : ViewModel() {

    // 1. UI State: Combines multiple flows into one "Source of Truth"
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    // 2. One-Time Events: For errors that should only show once (Snackbar)
    private val _errorChannel = Channel<String>()
    val errorEvents = _errorChannel.receiveAsFlow()

    init {
        // Start listening to the Repository
        observeRepository()

        // Connect to the server
        // (In a real app, you might get the URL from User Preferences)
        connectToSpace("ws://10.0.2.2:8080/orbital/device")
    }

    private fun observeRepository() {
        viewModelScope.launch {
            // Combine flows to update UI state safely
            combine(
                repository.deviceState,
                repository.connectionState
            ) { devices, connection ->
                DashboardUiState(
                    devices = devices,
                    connectionState = connection,
                    isLoading = connection is ConnectionState.Connecting
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }

        // Listen for Backend Errors (e.g. "Device Offline")
        viewModelScope.launch {
            repository.uiErrors.collect { error ->
                val message = when (error) {
                    is UiError.CommandRejected -> error.message
                    is UiError.ConnectionError -> "Connection Lost: ${error.message}"
                    is UiError.ParsingError -> "Data Error"
                }
                _errorChannel.send(message)
            }
        }
    }

    fun connectToSpace(url: String) {
        repository.connect(url)
    }

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