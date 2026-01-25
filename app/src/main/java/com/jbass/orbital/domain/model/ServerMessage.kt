package com.jbass.orbital.domain.model


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
    data class StateUpdate(
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