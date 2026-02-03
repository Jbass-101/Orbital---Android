package com.jbass.orbital.domain.model.weather

import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeather(
    val temperature: Double,
    val humidity: Int,
    val pressure: Int,
    val windSpeed: Double,
    val visibility: Int,
    val condition: WeatherCondition,
    val location: WeatherLocation
)

