package com.android.amazing_android_weather

import android.app.Application
import com.android.amazing_android_weather.core.appModule
import com.android.amazing_android_weather.core.commonModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WeatherApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WeatherApplication)
            modules(appModule)
            modules(commonModule)
        }
    }
}