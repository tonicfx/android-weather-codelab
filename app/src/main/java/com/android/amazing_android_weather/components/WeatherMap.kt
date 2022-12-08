package com.android.amazing_android_weather.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.android.amazing_android_weather.R
import com.android.amazing_android_weather.viewmodel.CityViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun WeatherMap(modifier: Modifier, viewModel: CityViewModel) {
    if (viewModel.hasCoordinate) {
        val description = LocalContext.current.resources.getString(
            R.string.city_description,
            viewModel.formattedCoordinate,
            viewModel.population
        )
        GoogleMap(
            modifier = modifier,
            cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(
                    LatLng(viewModel.lat, viewModel.lon),
                    viewModel.initialZoom
                )
            }
        ) {
            Marker(
                state = rememberMarkerState(position = LatLng(viewModel.lat, viewModel.lon)),
                title = viewModel.label,
                snippet = description
            )
        }
    }
}