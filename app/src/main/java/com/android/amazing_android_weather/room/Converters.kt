package com.android.amazing_android_weather.room

import androidx.room.TypeConverter
import com.android.amazing_android_weather.services.CoordinateDto
import com.android.amazing_android_weather.services.WeatherCityDto
import com.android.amazing_android_weather.services.WeatherInfoDto
import com.android.amazing_android_weather.services.WeatherItemDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromWeatherCity(value: WeatherCityDto): String {
        val gson = Gson()
        val type = object : TypeToken<WeatherCityDto>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toWeatherCity(value: String): WeatherCityDto {
        val gson = Gson()
        val type = object : TypeToken<WeatherCityDto>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromWeatherItem(value: List<WeatherItemDto>): String {
        val gson = Gson()
        val type = object : TypeToken<List<WeatherItemDto>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toWeatherItem(value: String): List<WeatherItemDto> {
        val gson = Gson()
        val type = object : TypeToken<List<WeatherItemDto>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromCoordinate(value: CoordinateDto): String {
        val gson = Gson()
        val type = object : TypeToken<CoordinateDto>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toCoordinate(value: String): CoordinateDto {
        val gson = Gson()
        val type = object : TypeToken<CoordinateDto>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromWeatherInfo(value: WeatherInfoDto): String {
        val gson = Gson()
        val type = object : TypeToken<WeatherInfoDto>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toWeatherInfo(value: String): WeatherInfoDto {
        val gson = Gson()
        val type = object : TypeToken<WeatherInfoDto>() {}.type
        return gson.fromJson(value, type)
    }
}