package com.android.amazing_android_weather.core

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.preferencesDataStore
import com.android.amazing_android_weather.WeatherApplication
import com.android.amazing_android_weather.repository.UserPreferencesRepositoryImpl
import com.android.amazing_android_weather.repository.UserPreferencesRepositoryInterface
import com.android.amazing_android_weather.repository.WeatherRepository
import com.android.amazing_android_weather.repository.WeatherRepositoryInterface
import com.android.amazing_android_weather.room.WeatherRoomDb
import com.android.amazing_android_weather.services.OpenWeatherApi
import com.android.amazing_android_weather.viewmodel.FavViewModel
import com.android.amazing_android_weather.viewmodel.LocationViewModel
import com.android.amazing_android_weather.viewmodel.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

private inline val requireApplication
    get() = WeatherApplication.instance ?: error("Missing call: initWith(application)")

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

val appModule = module {
    single<String>(named("weather_api_key")) { "888f70e84a4d7e44f3c0d4870c926e9d" }
    viewModel { WeatherViewModel(repository = get()) }

    single<FusedLocationProviderClient> {
        LocationServices.getFusedLocationProviderClient(
            requireApplication.applicationContext
        )
    }
    viewModel { LocationViewModel(client = get()) }

    viewModel { FavViewModel(get()) }
}

val commonModule = module {
    single { Android.create() }
    single { createJson() }
    single { createHttpClient(get(), get(), enableNetworkLogs = true) }
    single { CoroutineScope(Dispatchers.Default + SupervisorJob()) }

    val database by lazy { WeatherRoomDb.getDatabase(requireApplication) }

    single<WeatherRepositoryInterface> { WeatherRepository(get(), database.weatherDao()) }
    single { OpenWeatherApi(get(), get(named("weather_api_key"))) }
    single<UserPreferencesRepositoryInterface> { UserPreferencesRepositoryImpl(requireApplication.dataStore) }
}

fun createJson() = Json { isLenient = true; ignoreUnknownKeys = true }

fun createHttpClient(
    httpClientEngine: HttpClientEngine,
    json: Json,
    enableNetworkLogs: Boolean
) =
    HttpClient(httpClientEngine) {
        install(ContentNegotiation) {
            json(json)
        }
        if (enableNetworkLogs) {
            install(Logging) {
                logger = CustomHttpLogger()
                level = LogLevel.ALL
            }
        }
    }

class CustomHttpLogger() : Logger {
    override fun log(message: String) {
        Log.i("CustomHttpLogger", "message : $message")
    }
}
