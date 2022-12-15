package com.android.amazing_android_weather.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.android.amazing_android_weather.domain.FavCity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
) : UserPreferencesRepositoryInterface {

    private object Keys {
        val cities = stringPreferencesKey("cities")
    }

    private inline val Preferences.cities
        get() = this[Keys.cities] ?: ""

    override val favsCities: Flow<List<FavCity>> = dataStore.data.catch {
            // throws an IOException when an error is encountered when reading data
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map { preferences ->
            preferences.cities.split(",").map { FavCity(it) }
        }.distinctUntilChanged()

    override suspend fun addOrRemoveFavCity(favCity: FavCity) {
        dataStore.edit {
            val index = it[Keys.cities]?.split(",")?.indexOf(favCity.name)
            if (index != null && index != -1) {
                it[Keys.cities] =
                    it[Keys.cities]?.split(",")?.filter { cityName -> cityName != favCity.name }
                        ?.joinToString() ?: ""
            } else {
                it[Keys.cities] = (it[Keys.cities] ?: "") + favCity.name + ","
            }
        }
    }

    override suspend fun isInFavs(favCity: FavCity): Flow<Boolean> =
        dataStore.data.map { preferences ->
            preferences.cities.indexOf(favCity.name) != -1
        }.distinctUntilChanged()
}