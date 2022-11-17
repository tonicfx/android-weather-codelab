package com.android.amazing_android_weather.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.android.amazing_android_weather.R
import com.android.amazing_android_weather.viewmodel.WeatherViewModel
import com.android.amazing_android_weather.viewmodel.WeatherViewModelState
import org.koin.androidx.compose.getViewModel

@Composable
fun WeatherDetailsListScreen(
    navHostController: NavHostController,
    cityName: String
) {
    val weatherViewModel = getViewModel<WeatherViewModel>()
    val state by remember(weatherViewModel) {
        weatherViewModel.cityChanged(cityName)
    }.collectAsState(initial = WeatherViewModelState())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.list_title_label, cityName))
                },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            modifier = Modifier,
                            contentDescription = stringResource(id = R.string.icn_search_back_content_description)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            if (state.isLoading) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    CircularProgressIndicator()
                }
            } else if (state.items.isNotEmpty()) {
                WeatherList(weatherItems = state.items)
            }
        }
    }
}