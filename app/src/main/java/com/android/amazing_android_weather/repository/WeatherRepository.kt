package com.android.amazing_android_weather.repository

import com.android.amazing_android_weather.domain.WeatherResultDomain
import com.android.amazing_android_weather.domain.toDomain
import com.android.amazing_android_weather.services.OpenWeatherApi
import com.android.amazing_android_weather.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent

interface  WeatherRepositoryInterface {
    suspend fun fetchWeather(city: String): Flow<Resource<WeatherResultDomain?>>
    suspend fun fetchWeather(lat: Double, lon: Double): Flow<Resource<WeatherResultDomain?>>
}

class WeatherRepository(private val openWeatherApi: OpenWeatherApi) : KoinComponent,
    WeatherRepositoryInterface {

    override suspend fun fetchWeather(
        lat: Double,
        lon: Double
    ): Flow<Resource<WeatherResultDomain?>> {
        return fetch(null, lat, lon)
    }

    override suspend fun fetchWeather(city: String): Flow<Resource<WeatherResultDomain?>> {
        return fetch(city, null, null)
    }

    private suspend fun fetch(
        city: String?,
        lat: Double?,
        lon: Double?
    ): Flow<Resource<WeatherResultDomain?>> = flow {
        emit(Resource.Loading())
        if (city != null) {
            emit(Resource.Success(openWeatherApi.fetchWeather(city).toDomain()))
        } else emit(Resource.Success(openWeatherApi.fetchWeather(lat!!, lon!!).toDomain()))
    }
}