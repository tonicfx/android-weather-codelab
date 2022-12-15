package com.android.amazing_android_weather.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.amazing_android_weather.services.WeatherResultDto

@Dao
interface WeatherDao {
    @Query("SELECT * FROM WeatherResult")
    fun getAll(): List<WeatherResultDto>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(weatherResult: WeatherResultDto)

    @Query("DELETE FROM WeatherResult")
    fun deleteAll()
}