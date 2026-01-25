package com.jbass.orbital.domain.model

import kotlinx.serialization.Serializable

/**Device metadata*/
@Serializable
data class DeviceMetadata(
    val manufacturer: String,
    val model: String,
    val firmwareVersion: String,
    val ipAddress: String? = null,
    val macAddress: String? = null,
    val isReachable: Boolean,
    val lastSeenEpochMs: Long
)
