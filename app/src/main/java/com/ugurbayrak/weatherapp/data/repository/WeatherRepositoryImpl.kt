package com.ugurbayrak.weatherapp.data.repository

import com.ugurbayrak.weatherapp.data.remote.WeatherAPI
import com.ugurbayrak.weatherapp.data.remote.dto.forecast.ForecastResponse
import com.ugurbayrak.weatherapp.data.remote.dto.weather.WeatherResponse
import com.ugurbayrak.weatherapp.domain.repository.WeatherRepository
import retrofit2.Response
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherAPI: WeatherAPI
) : WeatherRepository {
    override suspend fun getWeatherByLatLon(lat: Double, lon: Double): Response<WeatherResponse> {
        return weatherAPI.getWeatherByLatLon(lat,lon)
    }

    override suspend fun getForecastByLatLon(lat: Double, lon: Double): Response<ForecastResponse> {
        return weatherAPI.getForecastByLatLon(lat, lon)
    }
}