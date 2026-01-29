package com.jbass.orbital.data.remote

import android.util.Log
import com.jbass.orbital.domain.model.ClientMessage
import com.jbass.orbital.domain.model.ConnectionState
import com.jbass.orbital.domain.model.ServerMessage
import com.jbass.orbital.domain.model.SmartDevice
import com.jbass.orbital.domain.model.UiError
import com.jbass.orbital.domain.model.Zone
import com.jbass.orbital.domain.model.weather.CurrentWeather
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.serialization.json.Json
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException
import kotlin.math.min

/**
 * Manages the persistent WebSocket connection.
 * Acts as the bridge between the Ktor Backend and the Android UI.
 */
class RealTimeClient(
    private val client: HttpClient,
    private val scope: CoroutineScope
) {

    /* ----------------------------
     * UI State
     * ---------------------------- */

    private val _deviceCache = mutableMapOf<String, SmartDevice>()
    //The Output: A hot stream of the current device list.
    private val _cachedDeviceState = MutableStateFlow<List<SmartDevice>>(emptyList())
    val deviceState: StateFlow<List<SmartDevice>> = _cachedDeviceState.asStateFlow()

    private val _weatherState =
        MutableStateFlow<CurrentWeather?>(null)

    val weatherState: StateFlow<CurrentWeather?> =
        _weatherState.asStateFlow()

    private val _zoneState =
        MutableStateFlow<List<Zone>>(emptyList())

    val zoneState: StateFlow<List<Zone>> = _zoneState.asStateFlow()


    //Connection Status
    private val _connectionStatus = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)
    val connectionStatus: StateFlow<ConnectionState> = _connectionStatus.asStateFlow()

    //SharedFlow for UI errors
    private val _uiErrors = MutableSharedFlow<UiError>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val uiErrors: SharedFlow<UiError> = _uiErrors.asSharedFlow()

    /* ----------------------------
     * Internal
     * ---------------------------- */

    // The Active Session
    private var session: DefaultClientWebSocketSession? = null
    // attempts reconnection
    private var reconnectJob: Job? = null
    private var shouldReconnect = true

    // Shared JSON config
    private val jsonConfig = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
        encodeDefaults = true
        classDiscriminator = "classType" //->Same a Backend
    }


    /* ----------------------------
     * Public API
     * ---------------------------- */

    /**
     * Opens the WebSocket connection and listens for incoming messages.
     * This function suspends until the connection is closed.
     */
    fun connect(serverUrl: String) {
        if(reconnectJob?.isActive == true) return // we are already trying to reconnect

        shouldReconnect = true

        reconnectJob = scope.launch {
            var attempt = 0

            _connectionStatus.value = ConnectionState.Connecting

            while (isActive && shouldReconnect){
                try {
                    openSocket(serverUrl)
                    attempt = 0 //connected
                } catch (e: Exception){
                    attempt++ //Connection attempt

                    if(!shouldReconnect) break

                    _connectionStatus.value = ConnectionState.Reconnecting(attempt)

                    _uiErrors.tryEmit(
                        UiError.ConnectionError(
                            "Connection lost. Reconnecting"
                        ))

                    delay(calculateBackoff(attempt))

                }
            }
            _connectionStatus.value = ConnectionState.Disconnected
        }
    }

    /**
     * Sends a command to the server.
     * Safe to call from ViewModels.
     */
    suspend fun sendCommand(command: ClientMessage) {
        val currentSession = session

        if (currentSession == null || !currentSession.isActive) {
            _uiErrors.tryEmit(
                UiError.ConnectionError(
                    "Not connected to server"
                ))
            return
        }

        try {
            // Polymorphic serialization requires the serializer argument
            val json = jsonConfig.encodeToString<ClientMessage>( command)
            currentSession.send(Frame.Text(json))
            Log.d("RealTimeClient", "Sent command: $json")
        } catch (e: Exception) {
            _uiErrors.tryEmit(
                UiError.CommandRejected(
                    e.message,
                    "Failed to send command"
                ))
        }
    }

    /**
     * Closes the connection manually (e.g., on User Logout).
     */
    suspend fun close() {
        session?.close()
        session = null
    }


    /* ----------------------------
     * WebSocket
     * ---------------------------- */

    private suspend fun openSocket(serverUrl: String){
        client.webSocket(serverUrl){
            session = this
            _connectionStatus.value = ConnectionState.Connected
            Log.d("RealTimeClient", "Connected, Yay!")

            try {
                for (frame in incoming){
                    if (frame is Frame.Text){
                        handleIncomingMessage(frame.readText())
                    }
                }

            }finally {
                cleanupSession()
                throw CancellationException("WebSocket closed")

            }
        }
    }


    /* ----------------------------
     * Message Handling
     * ---------------------------- */

    private fun handleIncomingMessage(jsonString: String) {
        try {
            val message = jsonConfig.decodeFromString<ServerMessage>(jsonString)

            when (message) {
                is ServerMessage.FullStateUpdate -> {
                    _deviceCache.clear()
                    message.devices.forEach { device ->
                        _deviceCache[device.id] = device
                    }
                    _cachedDeviceState.value = _deviceCache.values.toList()

                    _weatherState.value = message.weather

                    _zoneState.value = message.zones

                    Log.v("RealTimeClient", "State updated: ${message.devices.size} devices")
                }
                is ServerMessage.DeltaStateUpdate -> {
                    message.devices.forEach { device ->
                        _deviceCache[device.id] = device
                    }

                    _cachedDeviceState.value = _deviceCache.values.toList()

                }
                is ServerMessage.CommandAck -> {
                    if (!message.success) {
                        Log.w("RealTimeClient", "Command Rejected: ${message.errorCode}")
                        _uiErrors.tryEmit(
                            UiError.CommandRejected(
                                errorCode = message.message,
                                message = "Command Rejected by Server"
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("RealTimeClient", "Failed to parse incoming message: $jsonString", e)
            _uiErrors.tryEmit(
                UiError.ParsingError("Failed to parse incoming message")
            )
        }
    }

    /* ----------------------------
     * Helpers
     * ---------------------------- */

    private fun cleanupSession() {
        session = null
        Log.d("RealTimeClient", "Disconnected")
    }

    private fun calculateBackoff(attempt: Int): Long {
        val baseDelay = 1_000L
        val maxDelay = 30_000L
        return min(baseDelay * (1 shl (attempt - 1)), maxDelay)
    }
}
