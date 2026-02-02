package com.jbass.orbital.domain.model.message


import com.jbass.orbital.domain.model.device.SmartDevice
import com.jbass.orbital.domain.model.weather.CurrentWeather
import com.jbass.orbital.domain.model.zone.Zone
import kotlinx.serialization.Serializable

/**
 * Standard Error codes for the UI to interpret
 */
@Serializable
enum class ErrorCode {
    NONE,
    INVALID_COMMAND,
    DEVICE_NOT_FOUND,
    DEVICE_OFFLINE,
    INVALID_PAYLOAD,
    SERVER_ERROR
}

/**
 * All messages sent FROM Server TO Client are wrapped in this Sealed Class.
 * This allows the Client to listen for specific types of messages.
 */
@Serializable
sealed class ServerMessage {

    /**
     * Broadcast: Used to update the UI when a device changes state.
     */
    @Serializable
    data class FullStateUpdate(
        val weather: CurrentWeather,
        val devices: List<SmartDevice>,
        val zones: List<Zone>
    ) : ServerMessage()


    @Serializable
    data class DeltaStateUpdate(
        val devices: List<SmartDevice>
    ) : ServerMessage()

    /**
     * Direct Response: "The specific command (requestId) succeeded or failed."
     */
    @Serializable
    data class CommandAck(
        val requestId: String,
        val success: Boolean,
        val errorCode: ErrorCode = ErrorCode.NONE,
        val message: String? = null
    ) : ServerMessage()
}