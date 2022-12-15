package com.android.amazing_android_weather.repository

import com.android.amazing_android_weather.domain.FavCity
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepositoryInterface {
    val favsCities : Flow<List<FavCity>>

    suspend fun addOrRemoveFavCity(favCity : FavCity)

    suspend fun isInFavs(favCity : FavCity): Flow<Boolean>
}