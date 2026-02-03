package com.jbass.orbital.domain.model.weather

import kotlinx.serialization.Serializable

@Serializable
enum class WeatherCondition {
    CLEAR,
    RAIN,
    CLOUDS,
    SNOW,
    THUNDERSTORM,
    DRIZZLE,
}
