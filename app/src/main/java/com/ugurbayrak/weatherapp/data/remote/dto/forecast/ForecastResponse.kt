package com.ugurbayrak.weatherapp.data.remote.dto.forecast

data class ForecastResponse(
    val city: City,
    val cod: String,
    val list: List<Lists>,
    val message: Int
)