package com.ugurbayrak.weatherapp.data.remote.dto.forecast

data class Main(
    val feels_like: Double,
    val humidity: Int,
    val temp: Double,
    val temp_max: Double,
    val temp_min: Double
)