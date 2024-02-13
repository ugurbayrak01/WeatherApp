package com.ugurbayrak.weatherapp.data.util

import androidx.room.util.getColumnIndex
import com.ugurbayrak.weatherapp.data.remote.dto.forecast.ForecastResponse
import com.ugurbayrak.weatherapp.data.remote.dto.weather.WeatherResponse
import com.ugurbayrak.weatherapp.domain.model.Forecast
import com.ugurbayrak.weatherapp.domain.model.Weather
import java.util.Locale
import kotlin.math.roundToInt

fun WeatherResponse.toWeather() : Weather {
    return Weather(
        feelsLike = formatTemperature(main.feelsLike),
        humidity = "%" + main.humidity.toString(),
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

fun ForecastResponse.toForecast() : List<Forecast> {
    return list.map {
        Forecast(
            temp = formatTemperature(it.main.temp),
            pop = "%" + (it.pop * 100).toInt().toString(),
            dtTxt = getHour(it.dt_txt),
            icon = get2xIconUrl(it.weather[0].icon)
        )
    }
}

private fun get2xIconUrl(icon: String) = "https://openweathermap.org/img/wn/${icon}@2x.png"

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

private fun getHour(date: String) : String{
    var hour = date.substringAfter(" ").trim()
    hour = hour.substring(0, minOf(5, hour.length))
    return hour
}
