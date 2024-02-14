package com.ugurbayrak.weatherapp.presentation.weather.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ugurbayrak.weatherapp.databinding.FragmentWeatherBinding
import com.ugurbayrak.weatherapp.presentation.adapter.DailyForecastRecyclerAdapter
import com.ugurbayrak.weatherapp.presentation.adapter.HourlyForecastRecyclerAdapter
import com.ugurbayrak.weatherapp.presentation.weather.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
@AndroidEntryPoint
class WeatherFragment @Inject constructor() : Fragment() {

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: WeatherViewModel
    private var hourlyForecastRecyclerAdapter = HourlyForecastRecyclerAdapter()
    private var dailyForecastRecyclerAdapter = DailyForecastRecyclerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        viewModel.getWeatherByLatLon(36.5700117,35.3904988)

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
            }
            collectFlow()
        }
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