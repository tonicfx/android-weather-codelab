package com.android.amazing_android_weather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.amazing_android_weather.domain.FavCity
import com.android.amazing_android_weather.domain.WeatherCityDomain
import com.android.amazing_android_weather.repository.UserPreferencesRepositoryInterface
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

data class FavViewModelState(
    var currentFavCity: FavCity? = null,
    var isInFav: Boolean = false,
    var favs: List<FavCity> = emptyList(),
    var isLoading: Boolean = false,
)

class FavViewModel(private val userPreferences: UserPreferencesRepositoryInterface) : ViewModel() {
    fun load() = flow {
        userPreferences.favsCities.collect {
            emit(
                FavViewModelState(
                    favs = it.filter { fav -> fav.name.isNotEmpty() },
                    isLoading = false
                )
            )
        }
    }

    fun addOrRemoveCity(city: WeatherCityDomain) {
        viewModelScope.launch {
            userPreferences.addOrRemoveFavCity(FavCity(city.name))
            isInFav(city)
        }
    }

    fun isInFav(city: WeatherCityDomain) = flow {
        userPreferences.isInFavs(FavCity(city.name)).collect {
            emit(
                FavViewModelState(
                    currentFavCity = FavCity(city.name),
                    isInFav = it,
                    isLoading = false
                )
            )
        }
    }
}