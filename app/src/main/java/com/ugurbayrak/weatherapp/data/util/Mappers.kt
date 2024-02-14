package com.ugurbayrak.weatherapp.data.util

import com.ugurbayrak.weatherapp.data.remote.dto.forecast.ForecastResponse
import com.ugurbayrak.weatherapp.data.remote.dto.weather.WeatherResponse
import com.ugurbayrak.weatherapp.domain.model.DailyForecast
import com.ugurbayrak.weatherapp.domain.model.HourlyForecast
import com.ugurbayrak.weatherapp.domain.model.Weather
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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

fun ForecastResponse.toHourlyForecast() : List<HourlyForecast> {
    return list.map {
        HourlyForecast(
            temp = formatTemperature(it.main.temp),
            pop = "%" + (it.pop * 100).toInt().toString(),
            dtTxt = getHour(it.dt_txt),
            icon = get2xIconUrl(it.weather[0].icon)
        )
    }
}

fun ForecastResponse.toDailyForecast() : List<DailyForecast> {
    val forecastList = list.map {
        DailyForecastDummy(
            tempMin = it.main.temp_min,
            tempMax = it.main.temp_max,
            pop = it.pop,
            dtTxt = it.dt_txt,
            icon = it.weather[0].icon
        )
    }

    return formatDailyForecast(forecastList).map {
        DailyForecast(
            tempMin = formatTemperature(it.tempMin),
            tempMax = formatTemperature(it.tempMax),
            pop = "%" + (it.pop * 100).toInt().toString(),
            day = getDayOfWeek(it.dtTxt),
            icon = get2xIconUrl(it.icon),
            iconNight = get2xIconUrl(it.iconNight),
        )
    }
}

private data class DailyForecastDummy(
    val tempMin: Double,
    val tempMax: Double,
    val pop: Double,
    val dtTxt: String,
    val icon: String,
    val iconNight: String = "",
)

private fun formatDailyForecast(forecastList: List<DailyForecastDummy>): List<DailyForecastDummy> {
    val dailyGroups = forecastList.groupBy { it.dtTxt.substringBefore(" ").trim() }

    val dailyForecastList = dailyGroups.map { (date, dailyForecast) ->
        val tempMin = dailyForecast.minOf { it.tempMin }
        val tempMax = dailyForecast.maxOf { it.tempMax }
        val pop = dailyForecast.maxOf { it.pop }
        val icon = dailyForecast.find { it.dtTxt.endsWith("12:00:00") }?.icon ?: dailyForecast[0].icon
        val iconNight = dailyForecast.find { it.dtTxt.endsWith("18:00:00") }?.icon ?: dailyForecast[0].icon
        DailyForecastDummy(tempMin, tempMax, pop, date, icon, iconNight)
    }

    return dailyForecastList
}

private fun getDayOfWeek(date: String) : String{
    val today = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val calendar = Calendar.getInstance()
    calendar.time = dateFormat.parse(date)!!

    val dayOfWeek = when (calendar.get(Calendar.DAY_OF_WEEK)) {
        Calendar.SUNDAY -> "Sunday"
        Calendar.MONDAY -> "Monday"
        Calendar.TUESDAY -> "Tuesday"
        Calendar.WEDNESDAY -> "Wednesday"
        Calendar.THURSDAY -> "Thursday"
        Calendar.FRIDAY -> "Friday"
        Calendar.SATURDAY -> "Saturday"
        else -> "Unknown"
    }

    val isToday = today.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
            today.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)

    val day = if (isToday) {
        "Today"
    } else {
        dayOfWeek
    }

    return day
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
