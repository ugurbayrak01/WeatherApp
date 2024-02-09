package com.ugurbayrak.weatherapp.data.remote.dto.weather

data class WeatherResponse(
    val main: Main,
    val name: String,
    val sys: Sys,
    val weather: List<Weather>,
    val wind: Wind
)