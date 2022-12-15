package com.android.amazing_android_weather.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.amazing_android_weather.services.WeatherResultDto

@Database(entities = [WeatherResultDto::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class WeatherRoomDb : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: WeatherRoomDb? = null

        fun getDatabase(context: Context): WeatherRoomDb {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherRoomDb::class.java,
                    "weather_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}