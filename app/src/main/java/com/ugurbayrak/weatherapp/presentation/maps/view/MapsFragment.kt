package com.ugurbayrak.weatherapp.presentation.maps.view

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ugurbayrak.weatherapp.R
import com.ugurbayrak.weatherapp.databinding.FragmentMapsBinding
import com.ugurbayrak.weatherapp.presentation.MainActivity
import com.ugurbayrak.weatherapp.presentation.maps.MapsViewModel
import com.ugurbayrak.weatherapp.util.Constants.DEFAULT_LATITUDE
import com.ugurbayrak.weatherapp.util.Constants.DEFAULT_LONGITUDE
import com.ugurbayrak.weatherapp.util.Constants.PACKAGE_NAME
import com.ugurbayrak.weatherapp.util.Constants.PREFS_LATITUDE
import com.ugurbayrak.weatherapp.util.Constants.PREFS_LONGITUDE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class MapsFragment @Inject constructor() : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MapsViewModel
    private lateinit var mMap: GoogleMap
    private lateinit var sharedPrefs: SharedPreferences

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        val position = LatLng(
            getLatitudeFromSharedPreferences(),
            getLongitudeFromSharedPreferences()
        )
        googleMap.addMarker(MarkerOptions().position(position))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position,15f))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MapsViewModel::class.java]
        sharedPrefs = requireContext().getSharedPreferences(PACKAGE_NAME, MODE_PRIVATE)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        (requireActivity() as MainActivity).setSearchBarVisibility(View.VISIBLE)

        viewModel.getWeatherByLatLon(
            getLatitudeFromSharedPreferences(),
            getLongitudeFromSharedPreferences()
        )

        collectFlow()

        mapFragment?.getMapAsync { googleMap ->
            mMap = googleMap
        }

        mapFragment?.getMapAsync { googleMap ->
            googleMap.setOnMapLongClickListener {
                mMap.clear()
                mMap.addMarker(MarkerOptions().position(it))

                viewModel.getWeatherByLatLon(it.latitude,it.longitude)

                sharedPrefs.edit().putString(PREFS_LATITUDE,it.latitude.toString()).apply()
                sharedPrefs.edit().putString(PREFS_LONGITUDE,it.longitude.toString()).apply()
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
                }
            } else if(weatherState.error.isNotEmpty()) {
                Toast.makeText(requireContext(), weatherState.error, Toast.LENGTH_SHORT).show()
                binding.apply {
                    progressBar.visibility = View.GONE
                    weatherLinearLayout.visibility = View.GONE
                }
            } else {
                binding.apply {
                    weather = weatherState.weather
                    progressBar.visibility = View.GONE
                    weatherLinearLayout.visibility = View.VISIBLE
                }
            }
        }.launchIn(lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}