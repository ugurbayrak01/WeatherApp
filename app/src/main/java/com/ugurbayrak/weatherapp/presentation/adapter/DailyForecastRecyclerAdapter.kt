package com.ugurbayrak.weatherapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ugurbayrak.weatherapp.R
import com.ugurbayrak.weatherapp.databinding.DailyForecastRowBinding
import com.ugurbayrak.weatherapp.domain.model.DailyForecast

class DailyForecastRecyclerAdapter : RecyclerView.Adapter<DailyForecastRecyclerAdapter.DailyForecastViewHolder>() {

    class DailyForecastViewHolder(var binding: DailyForecastRowBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<DailyForecast>() {
        override fun areItemsTheSame(oldItem: DailyForecast, newItem: DailyForecast): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DailyForecast, newItem: DailyForecast): Boolean {
            return oldItem == newItem
        }
    }
    private val recyclerDiffer = AsyncListDiffer(this, diffUtil)

    var forecastList: List<DailyForecast>
        get() = recyclerDiffer.currentList
        set(value) = recyclerDiffer.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyForecastViewHolder {
        val binding = DataBindingUtil.inflate<DailyForecastRowBinding>(
            LayoutInflater.from(parent.context),
            R.layout.daily_forecast_row,
            parent,
            false
        )
        return DailyForecastViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return forecastList.size
    }

    override fun onBindViewHolder(holder: DailyForecastViewHolder, position: Int) {
        holder.binding.forecast = forecastList[position]
    }
}