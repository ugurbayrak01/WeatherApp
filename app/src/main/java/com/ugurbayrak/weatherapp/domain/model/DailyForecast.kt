package com.ugurbayrak.weatherapp.domain.model

data class DailyForecast (
    val tempMin: String,
    val tempMax: String,
    val pop: String,
    val day: String,
    val icon: String,
    val iconNight: String
)