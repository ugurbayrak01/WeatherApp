package com.ugurbayrak.weatherapp.domain.model

data class Weather(
    val feelsLike: String,
    val humidity: String,
    val temp: String,
    val tempMax: String,
    val tempMin: String,
    val name: String,
    val sunrise: Int,
    val sunset: Int,
    val description: String,
    val icon: String,
    val speed: Double
)
