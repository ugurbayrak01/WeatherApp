package com.ugurbayrak.weatherapp.presentation.maps

import com.ugurbayrak.weatherapp.domain.model.Weather

data class MapsState (
    val isLoading: Boolean = false,
    val weather: Weather? = null,
    val error: String = ""
)