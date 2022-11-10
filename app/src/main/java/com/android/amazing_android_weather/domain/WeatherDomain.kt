package com.android.amazing_android_weather.domain

import java.time.LocalDateTime

data class WeatherResultDomain(
    val city: WeatherCityDomain? = null,
    val items: List<WeatherItemDomain>? = listOf()
)

data class WeatherCityDomain(
    val name: String,
    val coord: CoordinateDomain,
    var population: Int,
    val sunrise: Double? = null,
    val sunset: Double? = null
)

data class CoordinateDomain(
    val lat: Double? = null,
    val lon: Double? = null
)

data class WeatherItemDomain(
    val date: LocalDateTime,
    val image: String,
    val infos: WeatherInfoDomain
) {
    lateinit var day: String
    lateinit var hour: String
}

data class WeatherInfoDomain(
    val temp: Double,
    val humidity: Double,
    val pressure: Double
)