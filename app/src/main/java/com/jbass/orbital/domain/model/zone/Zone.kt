package com.jbass.orbital.domain.model.zone

import kotlinx.serialization.Serializable

/**Device Location*/
@Serializable
data class Zone(
    val id: String,
    val name: String
)