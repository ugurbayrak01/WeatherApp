package com.ugurbayrak.weatherapp.presentation.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ugurbayrak.weatherapp.data.repository.WeatherRepositoryImpl
import com.ugurbayrak.weatherapp.data.util.toWeather
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

                if(weather.isSuccessful) {
                    weather.body()?.let {
                        _state.value = WeatherState(weather = it.toWeather())
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