package com.ugurbayrak.weatherapp.domain.model

data class Weather(
    val feelsLike: String,
    val humidity: String,
    val temp: String,
    val tempMax: String,
    val tempMin: String,
    val name: String,
    val sunrise: String,
    val sunset: String,
    val description: String,
    val icon: String,
    val speed: String
)
