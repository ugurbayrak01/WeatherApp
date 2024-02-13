package com.ugurbayrak.weatherapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ugurbayrak.weatherapp.R
import com.ugurbayrak.weatherapp.databinding.HourlyForecastColumnBinding
import com.ugurbayrak.weatherapp.domain.model.Forecast

class HourlyForecastRecyclerAdapter : RecyclerView.Adapter<HourlyForecastRecyclerAdapter.HourlyForecastViewHolder>() {

    class HourlyForecastViewHolder(var binding: HourlyForecastColumnBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Forecast>() {
        override fun areItemsTheSame(oldItem: Forecast, newItem: Forecast): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Forecast, newItem: Forecast): Boolean {
            return oldItem == newItem
        }

    }
    private val recyclerListDiffer = AsyncListDiffer(this, diffUtil)

    var forecastList: List<Forecast>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyForecastViewHolder {
        val binding = DataBindingUtil.inflate<HourlyForecastColumnBinding>(
            LayoutInflater.from(parent.context),
            R.layout.hourly_forecast_column,
            parent,
            false
        )
        return HourlyForecastViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return forecastList.size
    }

    override fun onBindViewHolder(holder: HourlyForecastViewHolder, position: Int) {
        holder.binding.forecast =forecastList[position]
    }
}