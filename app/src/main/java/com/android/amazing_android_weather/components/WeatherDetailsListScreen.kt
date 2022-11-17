package com.android.amazing_android_weather.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun WeatherDetailsListScreen(
    navHostController: NavHostController,
    cityName: String
) {
    Text(text = "hello details!")
}