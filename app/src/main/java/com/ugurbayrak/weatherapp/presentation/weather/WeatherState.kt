package com.ugurbayrak.weatherapp.presentation.weather

import com.ugurbayrak.weatherapp.domain.model.Weather

data class WeatherState (
    val isLoading: Boolean = false,
    val weather: Weather? = null,
    val error: String = ""
)