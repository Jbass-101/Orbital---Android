package com.jbass.orbital.data.remote

import android.util.Log
import com.jbass.orbital.domain.model.ClientMessage
import com.jbass.orbital.domain.model.ServerMessage
import com.jbass.orbital.domain.model.SmartDevice
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.json.Json
import kotlinx.coroutines.isActive

/**
 * Manages the persistent WebSocket connection.
 * Acts as the bridge between the Ktor Backend and the Android UI.
 */
class RealTimeClient(
    private val client: HttpClient
) {
    // 1. The Output: A hot stream of the current device list.
    private val _deviceState = MutableStateFlow<List<SmartDevice>>(emptyList())
    val deviceState: StateFlow<List<SmartDevice>> = _deviceState.asStateFlow()

    // 2. Connection Status
    private val _connectionStatus = MutableStateFlow(false)
    val connectionStatus: StateFlow<Boolean> = _connectionStatus.asStateFlow()

    // 3. The Active Session (Crucial for sending commands)
    private var session: DefaultClientWebSocketSession? = null

    // Shared JSON config
    private val jsonConfig = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    /**
     * Opens the WebSocket connection and listens for incoming messages.
     * This function suspends until the connection is closed.
     */
    suspend fun connect(serverUrl: String) {
        try {
            client.webSocket(serverUrl) {
                // Capture the session so sendCommand can use it
                session = this
                _connectionStatus.value = true
                Log.d("RealTimeClient", "Connected to Smart Space Server")

                try {
                    // Listen Loop
                    for (frame in incoming) {
                        if (frame is Frame.Text) {
                            val text = frame.readText()
                            handleIncomingMessage(text)
                        }
                    }
                } catch (e: Exception) {
                    Log.e("RealTimeClient", "Error reading frame", e)
                } finally {
                    // Cleanup when connection closes
                    _connectionStatus.value = false
                    session = null
                    Log.d("RealTimeClient", "Disconnected")
                }
            }
        } catch (e: Exception) {
            Log.e("RealTimeClient", "Failed to connect", e)
            _connectionStatus.value = false
            session = null
        }
    }

    /**
     * Sends a command to the server.
     * Safe to call from ViewModels.
     */
    suspend fun sendCommand(command: ClientMessage) {
        val currentSession = session

        if (currentSession == null || !currentSession.isActive) {
            Log.e("RealTimeClient", "Cannot send command: No active connection")
            return
        }

        try {
            // Polymorphic serialization requires the serializer argument
            val json = jsonConfig.encodeToString(ClientMessage.serializer(), command)
            currentSession.send(Frame.Text(json))
            Log.d("RealTimeClient", "Sent command: $json")
        } catch (e: Exception) {
            Log.e("RealTimeClient", "Failed to send command", e)
        }
    }

    /**
     * Closes the connection manually (e.g., on User Logout).
     */
    suspend fun close() {
        session?.close()
        session = null
    }

    private fun handleIncomingMessage(jsonString: String) {
        try {
            val message = jsonConfig.decodeFromString<ServerMessage>(jsonString)

            when (message) {
                is ServerMessage.StateUpdate -> {
                    _deviceState.value = message.devices
                    Log.v("RealTimeClient", "State updated: ${message.devices.size} devices")
                }
                is ServerMessage.CommandAck -> {
                    if (!message.success) {
                        Log.w("RealTimeClient", "Command Rejected: ${message.errorCode}")
                        // TODO: Emit to a SharedFlow<String> for UI Error Snackbars
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("RealTimeClient", "Failed to parse incoming message: $jsonString", e)
        }
    }
}