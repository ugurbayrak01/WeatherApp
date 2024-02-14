package com.ugurbayrak.weatherapp.presentation.weather

import com.ugurbayrak.weatherapp.domain.model.DailyForecast
import com.ugurbayrak.weatherapp.domain.model.HourlyForecast
import com.ugurbayrak.weatherapp.domain.model.Weather

data class WeatherState (
    val isLoading: Boolean = false,
    val weather: Weather? = null,
    val hourlyForecast: List<HourlyForecast> = emptyList(),
    val dailyForecast: List<DailyForecast> = emptyList(),
    val error: String = ""
)