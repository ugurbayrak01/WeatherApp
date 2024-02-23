package com.ugurbayrak.weatherapp.presentation.weather.view

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.ktx.widget.PlaceSelectionError
import com.google.android.libraries.places.ktx.widget.PlaceSelectionSuccess
import com.google.android.libraries.places.ktx.widget.placeSelectionEvents
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.ugurbayrak.weatherapp.R
import com.ugurbayrak.weatherapp.databinding.FragmentWeatherBinding
import com.ugurbayrak.weatherapp.presentation.adapter.DailyForecastRecyclerAdapter
import com.ugurbayrak.weatherapp.presentation.adapter.HourlyForecastRecyclerAdapter
import com.ugurbayrak.weatherapp.presentation.weather.WeatherViewModel
import com.ugurbayrak.weatherapp.util.Constants.DEFAULT_LATITUDE
import com.ugurbayrak.weatherapp.util.Constants.DEFAULT_LONGITUDE
import com.ugurbayrak.weatherapp.util.Constants.PACKAGE_NAME
import com.ugurbayrak.weatherapp.util.Constants.PREFS_LATITUDE
import com.ugurbayrak.weatherapp.util.Constants.PREFS_LONGITUDE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class WeatherFragment @Inject constructor() : Fragment() {

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: WeatherViewModel
    private var hourlyForecastRecyclerAdapter = HourlyForecastRecyclerAdapter()
    private var dailyForecastRecyclerAdapter = DailyForecastRecyclerAdapter()
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        sharedPrefs = requireContext().getSharedPreferences(PACKAGE_NAME, MODE_PRIVATE)

        _binding?.let {
            binding.apply {
                hourlyForecastRecyclerview.adapter = hourlyForecastRecyclerAdapter
                hourlyForecastRecyclerview.layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                dailyForecastRecyclerview.adapter = dailyForecastRecyclerAdapter
                dailyForecastRecyclerview.layoutManager = LinearLayoutManager(requireContext())

                swipeRefreshLayout.setOnRefreshListener {
                    swipeRefreshLayout.isRefreshing = false
                    viewModel.getWeatherByLatLon(
                        getLatitudeFromSharedPreferences(),
                        getLongitudeFromSharedPreferences()
                    )
                }
            }

            viewModel.getWeatherByLatLon(
                getLatitudeFromSharedPreferences(),
                getLongitudeFromSharedPreferences()
            )

            collectFlow()
        }

        Places.initialize(requireContext(), getString(R.string.google_maps_key))

        val autocompleteFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(
            listOf(Place.Field.NAME, Place.Field.ID, Place.Field.LAT_LNG, Place.Field.ADDRESS)
        )

        lifecycleScope.launch {
            autocompleteFragment.placeSelectionEvents().collect { event ->
                when (event) {
                    is PlaceSelectionSuccess -> {
                        val latitude = event.place.latLng?.latitude
                        val longitude = event.place.latLng?.longitude

                        sharedPrefs.edit().putString(PREFS_LATITUDE,latitude.toString()).apply()
                        sharedPrefs.edit().putString(PREFS_LONGITUDE,longitude.toString()).apply()

                        viewModel.getWeatherByLatLon(
                            getLatitudeFromSharedPreferences(),
                            getLongitudeFromSharedPreferences()
                        )
                    }
                    is PlaceSelectionError -> {}
                }
            }
        }
    }

    private fun getLatitudeFromSharedPreferences() : Double{
        return sharedPrefs.getString(PREFS_LATITUDE, DEFAULT_LATITUDE)!!.toDouble()
    }

    private fun getLongitudeFromSharedPreferences()  : Double {
        return sharedPrefs.getString(PREFS_LONGITUDE, DEFAULT_LONGITUDE)!!.toDouble()
    }

    private fun collectFlow() {
        viewModel.state.onEach { weatherState ->
            if(weatherState.isLoading) {
                binding.apply {
                    progressBar.visibility = View.VISIBLE
                    weatherLinearLayout.visibility = View.GONE
                    hourlyForecastRecyclerview.visibility = View.GONE
                    dailyForecastRecyclerview.visibility = View.GONE
                }
            } else if(weatherState.error.isNotEmpty()) {
                Toast.makeText(requireContext(), weatherState.error, Toast.LENGTH_SHORT).show()
                binding.apply {
                    progressBar.visibility = View.GONE
                    weatherLinearLayout.visibility = View.GONE
                    hourlyForecastRecyclerview.visibility = View.GONE
                    dailyForecastRecyclerview.visibility = View.GONE
                }
            } else {
                binding.apply {
                    weather = weatherState.weather
                    progressBar.visibility = View.GONE
                    weatherLinearLayout.visibility = View.VISIBLE
                    hourlyForecastRecyclerview.visibility = View.VISIBLE
                    dailyForecastRecyclerview.visibility = View.VISIBLE
                }
                hourlyForecastRecyclerAdapter.forecastList = weatherState.hourlyForecast
                dailyForecastRecyclerAdapter.forecastList = weatherState.dailyForecast
            }
        }.launchIn(lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}