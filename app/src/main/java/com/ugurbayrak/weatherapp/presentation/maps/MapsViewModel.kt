package com.ugurbayrak.weatherapp.presentation.maps

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
class MapsViewModel @Inject constructor(
    private val repository: WeatherRepositoryImpl
) : ViewModel() {
    private val _state = MutableStateFlow(MapsState())
    val state : StateFlow<MapsState>
        get() = _state

    fun getWeatherByLatLon(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                _state.value = MapsState(isLoading = true)
                val weather = repository.getWeatherByLatLon(lat, lon)

                if(weather.isSuccessful) {
                    weather.body()?.let {
                        _state.value = MapsState(weather = it.toWeather())
                    }
                } else {
                    _state.value = MapsState(error = "Not found")
                }
            } catch (e: Exception) {
                _state.value = MapsState(error = e.localizedMessage ?: "Error")
            }
        }
    }
}