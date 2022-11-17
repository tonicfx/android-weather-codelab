package com.android.amazing_android_weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.android.amazing_android_weather.components.SetupNavGraph
import com.android.amazing_android_weather.components.WeatherSearchScreen
import com.android.amazing_android_weather.ui.theme.AmazingandroidweatherTheme
import com.android.amazing_android_weather.viewmodel.WeatherViewModel
import com.android.amazing_android_weather.viewmodel.WeatherViewModelState
import org.koin.androidx.compose.getViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AmazingandroidweatherTheme {
                val navHostController = rememberNavController()
                SetupNavGraph(navHostController = navHostController)
            }
        }
    }
}


@Composable
fun WeatherList(name: String) {
    val weatherViewModel = getViewModel<WeatherViewModel>()
    val state by remember(weatherViewModel) {
        weatherViewModel.cityChanged(name)
    }.collectAsState(initial = WeatherViewModelState())

    Column() {
        Text(text = "Hello $name!")
        state.items.let {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                items(items = it, itemContent = { item ->
                    Row() {
                        Text(text = "Le ${item.day} à ${item.hour}H ==> ")
                        Text(
                            text =
                            when (item.image) {
                                "1" -> "beau"
                                "2" -> "moyen"
                                "3" -> "pluie"
                                else -> ":/"
                            }
                        )
                    }
                })
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AmazingandroidweatherTheme {
        Greeting("Android")
    }
}