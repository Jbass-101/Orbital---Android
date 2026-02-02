package com.jbass.orbital.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jbass.orbital.data.remote.NetworkDiscovery
import com.jbass.orbital.domain.model.*
import com.jbass.orbital.domain.model.device.DeviceCategory
import com.jbass.orbital.domain.model.device.DeviceState
import com.jbass.orbital.domain.model.device.SmartDevice
import com.jbass.orbital.domain.model.message.ClientMessage
import com.jbass.orbital.domain.repository.RealTimeClientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: RealTimeClientRepository,
    private val discovery: NetworkDiscovery
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState = _uiState.asStateFlow()

    // One-time events (Snackbars, Navigation)
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        // 1. Scan for server
        scanForServer()

        // 2. Combine Repository Flows into UI State
        viewModelScope.launch {
            combine(
                repository.deviceState,
                repository.weatherState,
                repository.zoneState,
                repository.connectionState,
                _uiState.map { it.selectedCategory }.distinctUntilChanged()
            ) { devices, weather,rooms,connState, category ->

                //get all available categories
                val availableCategories = DeviceCategory.entries.filter { category ->
                    devices.any { it.type.category == category }
                }

                //Moved sorted devices to top
                val sortedDevices = devices.sortedBy { it.zoneId }
                //If a category is selected, return the filtered devices
                val filtered = category?.let {
                    sortedDevices.filter { device -> device.type.category == it }
                } ?: sortedDevices

                DashboardUiState(
                    devices = sortedDevices,
                    rooms = rooms,
                    filteredDevices = filtered,
                    weather = weather,
                    categories = availableCategories,
                    selectedCategory = category,
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

    private fun scanForServer() {
        viewModelScope.launch {
            // 1. Start a 5-second timer
            val timeoutJob = launch {
                delay(5000)
                // If we are still disconnected after 5s, show the input
                if (_uiState.value.connectionState !is ConnectionState.Connected) {
                    _uiState.update { it.copy(showManualInput = true) }
                }
            }

            // 2. Start Listening for mDNS
            discovery.discoverService()
                .collect { foundUrl ->
                    // SERVER FOUND!
                    timeoutJob.cancel() // Cancel the fallback timer
                    _uiState.update { it.copy(serverUrl = foundUrl, showManualInput = false) }
                    connect(foundUrl)
                }
        }
    }
    fun onManualIpEntered(ip: String) {
        val url = "ws://$ip:58080/orbital/device"
        connect(url)
        _uiState.update { it.copy(showManualInput = false) }
    }

    fun showManualInput(){
        _uiState.update { it.copy(showManualInput = true) }

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

    fun onRoomSelected(roomId: String?) {

        //@Todo Maybe validate with servr before updating ui?
        _uiState.update {
            it.copy(
                selectedRoomId = roomId,
                selectedCategory = null
            )
        }


        viewModelScope.launch {
            val command = ClientMessage.Subscribe(
                requestId = UUID.randomUUID().toString(),
                subscribeZones = roomId?.let { setOf(it) } ?: emptySet(),
                unsubscribeZones = emptySet()
            )
            repository.sendCommand(command)
        }

    }

    fun onFilter(category: DeviceCategory?){
        _uiState.update {
            it.copy(selectedCategory = category)
        }
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