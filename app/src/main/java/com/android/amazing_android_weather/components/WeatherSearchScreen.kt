package com.android.amazing_android_weather.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.android.amazing_android_weather.R
import com.android.amazing_android_weather.viewmodel.CityViewModel
import com.android.amazing_android_weather.viewmodel.WeatherViewModel
import com.android.amazing_android_weather.viewmodel.WeatherViewModelState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun WeatherSearchScreen(
    navHostController: NavHostController
) {
    var job: Job? = null
    var text by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    val weatherViewModel = getViewModel<WeatherViewModel>()

    var locationSearching by remember { mutableStateOf(false) }

    var state by remember {
        mutableStateOf(WeatherViewModelState())
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("") }, actions = {
                WeatherSearchBar(
                    searchText = text,
                    placeholderText = stringResource(id = R.string.search_placeholder),
                    onSearchTextChanged = { it ->
                        text = it
                        job?.cancel()
                        job = CoroutineScope(Dispatchers.IO).launch {
                            weatherViewModel.cityChanged(text).collect {
                                state = it
                            }
                        }
                    },
                    onClearClick = {
                        text = ""
                        state = WeatherViewModelState()
                    },
                    onLocateSearching = {
                        locationSearching = it
                    },
                    onLocateChange = {
                        CoroutineScope(Dispatchers.IO).launch {
                            weatherViewModel.locationChanged(it).collect {
                                state = it
                            }
                        }
                    }
                )
            })
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                }
        ) {
            if (state.isLoading || locationSearching) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp)
                ) {
                    CircularProgressIndicator()
                    Text(
                        text = stringResource(
                            id =  if (state.isLoading) {
                                R.string.search_processing_label
                            } else {
                                R.string.localisation_processing_label
                            }
                        )
                    )
                }
            }  else if (state.city != null && state.first != null) {
                val city = state.city!!
                val item = state.first!!

                WeatherMap(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                        .weight(1f),
                    viewModel = CityViewModel(city = city)
                )
                WeatherDetailsItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .weight(2f),
                    city = city,
                    item = item
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End
                ) {
                    Button(onClick = {
                        navHostController.navigate(route = "${Screen.WeatherList.route}/${city.name}")
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowForward,
                            contentDescription = stringResource(id = R.string.icn_go_to_details)
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(
                            id = R.drawable.ic_weather_question
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(100.dp)
                            .height(100.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.entry_label),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(30.dp),
                        style = MaterialTheme.typography.h5,
                    )
                }
            }
        }
    }
}