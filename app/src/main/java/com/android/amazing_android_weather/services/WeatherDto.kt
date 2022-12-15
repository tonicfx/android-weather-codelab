package com.android.amazing_android_weather.services

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "WeatherResult")
@Serializable
data class WeatherResultDto(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val city: WeatherCityDto? = null,
    val list: List<WeatherItemDto>? = listOf()
)

@Serializable
data class WeatherCityDto(
    val name: String,
    val coord: CoordinateDto,
    var population: Int,
    val sunrise: Double? = null,
    val sunset: Double? = null
)

@Serializable
data class CoordinateDto(val lat: Double? = null, val lon: Double? = null)

@Serializable
data class WeatherItemDto(val dt_txt: String, val dt: Float, val main: WeatherInfoDto)

@Serializable
data class WeatherInfoDto(val temp: Double, val humidity: Double, val pressure: Double)