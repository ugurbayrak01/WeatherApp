package com.ugurbayrak.weatherapp.presentation.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ugurbayrak.weatherapp.data.repository.WeatherRepositoryImpl
import com.ugurbayrak.weatherapp.data.util.toDailyForecast
import com.ugurbayrak.weatherapp.data.util.toHourlyForecast
import com.ugurbayrak.weatherapp.data.util.toWeather
import com.ugurbayrak.weatherapp.domain.model.DailyForecast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepositoryImpl
) :ViewModel() {
    private val _state = MutableStateFlow(WeatherState())
    val state: StateFlow<WeatherState>
        get() = _state

    fun getWeatherByLatLon(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                _state.value = WeatherState(isLoading = true)
                val weather = repository.getWeatherByLatLon(lat, lon)
                val forecast = repository.getForecastByLatLon(lat, lon)

                if(weather.isSuccessful && forecast.isSuccessful) {
                    weather.body()?.let {
                        _state.value = _state.value.copy(weather = it.toWeather())
                    }

                    forecast.body()?.let {
                        _state.value = _state.value.copy(hourlyForecast = it.toHourlyForecast().take(8))
                        _state.value = _state.value.copy(dailyForecast = it.toDailyForecast().take(5), isLoading = false)
                    }
                } else {
                    _state.value = WeatherState(error = "Not found")
                }
            } catch (e: Exception) {
                _state.value = WeatherState(error = e.localizedMessage ?: "Error")
            }
        }
    }
}