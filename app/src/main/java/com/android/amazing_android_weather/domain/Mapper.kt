package com.android.amazing_android_weather.domain

import com.android.amazing_android_weather.services.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun WeatherResultDto.toDomain() = WeatherResultDomain(
    city = this.city?.toDomain(),
    items = this.list?.map { it.toDomain() },
)

fun WeatherCityDto.toDomain() = WeatherCityDomain(
    name = this.name,
    coord = this.coord.toDomain(),
    population = this.population,
    sunrise = this.sunrise,
    sunset = this.sunset
)

fun CoordinateDto.toDomain() = CoordinateDomain(
    lat = this.lat,
    lon = this.lon
)

fun WeatherItemDto.toDomain() = WeatherItemDomain(
    date = LocalDateTime.parse(this.dt_txt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
    image = when {
        main.humidity < 60 -> "1"
        main.humidity < 80 -> "2"
        else -> "3"
    },
    infos = this.main.toDomain()
).apply {
    this.day = date.dayOfMonth.toString()
    this.hour = date.hour.toString()
}

fun WeatherInfoDto.toDomain() = WeatherInfoDomain(
    temp = this.temp,
    humidity = this.humidity,
    pressure = this.pressure
)