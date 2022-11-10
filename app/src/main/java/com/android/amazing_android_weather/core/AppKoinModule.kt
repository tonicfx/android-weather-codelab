package com.android.amazing_android_weather.core

import android.util.Log
import com.android.amazing_android_weather.repository.WeatherRepository
import com.android.amazing_android_weather.repository.WeatherRepositoryInterface
import com.android.amazing_android_weather.services.OpenWeatherApi
import com.android.amazing_android_weather.viewmodel.WeatherViewModel
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

val appModule = module {
    single<String>(named("weather_api_key")) { "888f70e84a4d7e44f3c0d4870c926e9d" }
    viewModel { WeatherViewModel(repository = get()) }
}
val commonModule = module {
    single { Android.create() }
    single { createJson() }
    single { createHttpClient(get(), get(), enableNetworkLogs = true) }
    single { CoroutineScope(Dispatchers.Default + SupervisorJob()) }
    single<WeatherRepositoryInterface> { WeatherRepository(get()) }
    single { OpenWeatherApi(get(), get(named("weather_api_key"))) }
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
