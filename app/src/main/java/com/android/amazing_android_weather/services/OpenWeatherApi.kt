package com.android.amazing_android_weather.services

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.koin.core.component.KoinComponent

class OpenWeatherApi(
    private val client: HttpClient,
    private val apiKey : String,
    var baseUrl: String = "https://api.openweathermap.org/data/2.5",
) : KoinComponent {

    suspend fun fetchWeather(city: String) = client.get("$baseUrl/forecast") {
        url {
            parameters.append("q", "$city, FR")
            parameters.append("APPID", apiKey)
            parameters.append("units", "metric")
        }
    }.body<WeatherResultDto>()

    suspend fun fetchWeather(lat: Double, lon : Double) = client.get("$baseUrl/forecast") {
        url {
            parameters.append("lat", "$lat")
            parameters.append("lon", "$lon")
            parameters.append("APPID", apiKey)
            parameters.append("units", "metric")
        }
    }.body<WeatherResultDto>()
}
