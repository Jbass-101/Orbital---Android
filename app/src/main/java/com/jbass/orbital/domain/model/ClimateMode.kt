package com.jbass.orbital.domain.model


import kotlinx.serialization.Serializable

/**This is specific to the climate device types*/
@Serializable
enum class ClimateMode {
    HEAT,
    COOL,
    AUTO,
    OFF
}
