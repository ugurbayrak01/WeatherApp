package com.ugurbayrak.weatherapp.data.remote.dto.forecast

data class City(
    val country: String,
    val id: Int,
    val name: String,
    val sunrise: Int,
    val sunset: Int,
    val timezone: Int
)