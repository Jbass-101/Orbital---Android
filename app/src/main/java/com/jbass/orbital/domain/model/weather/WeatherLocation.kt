package com.jbass.orbital.domain.model.weather

import kotlinx.serialization.Serializable

@Serializable
data class WeatherLocation(
    val city: String,
    val country: String,
)