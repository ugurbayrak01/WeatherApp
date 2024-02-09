package com.ugurbayrak.weatherapp.presentation.weather.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.ugurbayrak.weatherapp.R
import com.ugurbayrak.weatherapp.databinding.FragmentWeatherBinding
import com.ugurbayrak.weatherapp.presentation.weather.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import retrofit2.http.Query
import javax.inject.Inject
@AndroidEntryPoint
class WeatherFragment @Inject constructor() : Fragment() {

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: WeatherViewModel

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
            collectFlow()
        }
    }

    private fun collectFlow() {
        viewModel.state.onEach { weatherState ->
            if(weatherState.isLoading) {
                binding.progressBar.visibility = View.VISIBLE
                binding.weatherLinearLayout.visibility = View.GONE
            } else if(weatherState.error.isNotEmpty()) {
                Toast.makeText(requireContext(), weatherState.error, Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
                binding.weatherLinearLayout.visibility = View.GONE
            } else {
                binding.weather = weatherState.weather
                binding.progressBar.visibility = View.GONE
                binding.weatherLinearLayout.visibility = View.VISIBLE
            }
        }.launchIn(lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}