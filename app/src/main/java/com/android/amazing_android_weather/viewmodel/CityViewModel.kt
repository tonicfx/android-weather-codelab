package com.android.amazing_android_weather.viewmodel

import androidx.lifecycle.ViewModel
import com.android.amazing_android_weather.domain.WeatherCityDomain
import com.android.amazing_android_weather.utils.LocationFormater

class CityViewModel(private val city: WeatherCityDomain): ViewModel()  {
    val initialZoom = 12f

    val label: String = city.name

    val population : Int
        get() = city.population

    val lat: Double = city.coord.lat ?: 0.0

    val lon: Double = city.coord.lon ?: 0.0

    val hasCoordinate: Boolean
        get() = city.coord.lat != null && city.coord.lon != null

    val formattedCoordinate: String
        get() {
            return if (hasCoordinate) {
                "${
                    LocationFormater.latitudeAsDMS(city.coord.lat!!, 2)
                }  ${
                    LocationFormater.longitudeAsDMS(
                        city.coord.lon!!, 2
                    )
                }"
            } else ""
        }
}