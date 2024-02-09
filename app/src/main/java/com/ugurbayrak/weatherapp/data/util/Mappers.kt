package com.ugurbayrak.weatherapp.data.util

import com.ugurbayrak.weatherapp.data.remote.dto.weather.WeatherResponse
import com.ugurbayrak.weatherapp.domain.model.Weather
import java.util.Locale

fun WeatherResponse.toWeather() : Weather {
    return Weather(
        feelsLike = formatTemperature(main.feelsLike),
        humidity = main.humidity,
        temp = formatTemperature(main.temp),
        tempMax = formatTemperature(main.tempMax),
        tempMin = formatTemperature(main.tempMin),
        name = formatString(name),
        sunrise = sys.sunrise,
        sunset = sys.sunset,
        description = formatString(weather[0].description),
        icon = get4xIconUrl(weather[0].icon),
        speed = wind.speed
    )
}

private fun get4xIconUrl(icon: String) = "https://openweathermap.org/img/wn/${icon}@4x.png"

private fun formatTemperature(temp: Double) : String {
    val tempString = temp.toInt().toString()
    return "$tempString\u00B0"
}

private fun formatString(string: String): String {
    val words = string.split(" ")
    val capitalizedWords = words.map {
        it.replaceFirstChar(Char::titlecase)
    }
    return capitalizedWords.joinToString(" ")
}
