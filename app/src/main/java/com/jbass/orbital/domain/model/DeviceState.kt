package com.jbass.orbital.domain.model


import kotlinx.serialization.Serializable

/**There are so many smart devices out there, so I added an extensible list of status, it's a sealed class and not a string foe type safety*/
@Serializable
sealed class DeviceState {

    @Serializable
    data class OnOff(
        val isOn: Boolean
    ) : DeviceState()

    @Serializable
    data class Level(
        val value: Int
    ) : DeviceState()

    @Serializable
    data class Temperature(
        val current: Float,
        val target: Float,
        val mode: ClimateMode
    ) : DeviceState()

    @Serializable
    data class Media(
        val isOn: Boolean,
        val volume: Int,
        val source: String
    ) : DeviceState()

    @Serializable
    data class Position(
        val position: Int
    ) : DeviceState()

    @Serializable
    data class Network(
        val online: Boolean,
        val latencyMs: Int?
    ) : DeviceState()

    @Serializable
    object Offline : DeviceState()
}
