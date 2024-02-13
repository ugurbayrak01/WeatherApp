package com.ugurbayrak.weatherapp.data.remote.dto.forecast

data class Lists(
    val dt: Int,
    val dt_txt: String,
    val main: Main,
    val pop: Double,
    val sys: Sys,
    val weather: List<Weather>,
    val wind: Wind
)