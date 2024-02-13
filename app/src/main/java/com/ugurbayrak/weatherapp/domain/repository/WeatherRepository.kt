package com.ugurbayrak.weatherapp.domain.repository

import com.ugurbayrak.weatherapp.data.remote.dto.forecast.ForecastResponse
import com.ugurbayrak.weatherapp.data.remote.dto.weather.WeatherResponse
import retrofit2.Response

interface WeatherRepository {
    suspend fun getWeatherByLatLon(lat: Double, lon: Double) : Response<WeatherResponse>
    suspend fun getForecastByLatLon(lat: Double, lon: Double) : Response<ForecastResponse>
}