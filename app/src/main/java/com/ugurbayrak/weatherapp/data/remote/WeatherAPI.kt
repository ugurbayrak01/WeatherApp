package com.ugurbayrak.weatherapp.data.remote

import com.ugurbayrak.weatherapp.data.remote.dto.forecast.ForecastResponse
import com.ugurbayrak.weatherapp.data.remote.dto.weather.WeatherResponse
import com.ugurbayrak.weatherapp.util.ApiKey.API_KEY
import com.ugurbayrak.weatherapp.util.Constants.UNITS
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("/data/2.5/weather")
    suspend fun getWeatherByLatLon(
        @Query("lat") lat: Double = 36.5700117,
        @Query("lon") lon: Double = 35.3904988,
        @Query("units") units: String = UNITS,
        @Query("appid") appId: String = API_KEY
    ) : Response<WeatherResponse>

    @GET("/data/2.5/forecast")
    suspend fun getForecastByLatLon(
        @Query("lat") lat: Double = 36.5700117,
        @Query("lon") lon: Double = 35.3904988,
        @Query("units") units: String = UNITS,
        @Query("appid") appId: String = API_KEY
    ) : Response<ForecastResponse>
}