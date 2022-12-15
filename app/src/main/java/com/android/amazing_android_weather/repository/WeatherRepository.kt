package com.android.amazing_android_weather.repository

import android.util.Log
import com.android.amazing_android_weather.domain.WeatherResultDomain
import com.android.amazing_android_weather.domain.toDomain
import com.android.amazing_android_weather.room.WeatherDao
import com.android.amazing_android_weather.services.OpenWeatherApi
import com.android.amazing_android_weather.utils.Resource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import java.util.*

interface  WeatherRepositoryInterface {
    suspend fun fetchWeather(city: String): Flow<Resource<WeatherResultDomain?>>
    suspend fun fetchWeather(lat: Double, lon: Double): Flow<Resource<WeatherResultDomain?>>
}

fun CoroutineScope.launchPeriodicAsync(
    repeatMillis: Long,
    action: () -> Unit
) = this.async {
    if (repeatMillis > 0) {
        while (isActive) {
            action()
            delay(repeatMillis)
        }
    } else {
        action()
    }
}

class WeatherRepository(private val openWeatherApi: OpenWeatherApi, private val weatherDao: WeatherDao) : KoinComponent,
    WeatherRepositoryInterface {

    init {
        startWeatherClear()
    }

    private fun startWeatherClear() {
        CoroutineScope(Dispatchers.IO).launchPeriodicAsync(30000) {
            weatherDao.deleteAll()
            Log.d("WeatherRepository", "WeatherRepository delete cache")
        }
    }

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
            var result = weatherDao.getAll()
                .firstOrNull { it.city?.name?.lowercase(Locale.FRANCE) == city.lowercase(Locale.FRANCE) }
            if (result == null) {
                result = openWeatherApi.fetchWeather(city)
                if (result.city != null) {
                    weatherDao.insert(result)
                    Log.d("WeatherRepository", "WeatherRepository insert in cache")
                }
            } else {
                Log.d("WeatherRepository", "WeatherRepository use cache")
            }

            emit(Resource.Success(result.toDomain()))
        } else emit(Resource.Success(openWeatherApi.fetchWeather(lat!!, lon!!).toDomain()))
    }
}