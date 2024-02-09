package com.ugurbayrak.weatherapp.di

import com.ugurbayrak.weatherapp.data.remote.WeatherAPI
import com.ugurbayrak.weatherapp.data.repository.WeatherRepositoryImpl
import com.ugurbayrak.weatherapp.domain.repository.WeatherRepository
import com.ugurbayrak.weatherapp.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideWeatherAPI() : WeatherAPI {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideWeatherRepositoryImpl(weatherAPI: WeatherAPI) : WeatherRepository {
        return WeatherRepositoryImpl(weatherAPI)
    }
}