package com.android.amazing_android_weather.components

import android.Manifest
import android.location.Location
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.android.amazing_android_weather.R
import com.android.amazing_android_weather.viewmodel.LocationViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@Composable
fun LocationPermissionIcon(
    locationSearching: (search: Boolean) -> Unit,
    locationChange: (location: Location) -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val locationViewModel = getViewModel<LocationViewModel>()

    val askLocationPermission by locationViewModel.askLocationPermission.collectAsState()

    if (askLocationPermission) {
        PermissionPopupSystem(
            context,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            stringResource(id = R.string.permission_location_rationale),
        ) { permissionAction ->
            when (permissionAction) {
                is PermissionAction.OnPermissionGranted -> {
                    locationViewModel.launchLocationPermission(false)
                    scope.launch {
                        locationViewModel.locate().collect {
                            locationSearching(it.isLocationSearching)
                            if (it.location != null) {
                                locationChange(it.location!!)
                                this.cancel()
                            }
                        }
                    }
                }
                is PermissionAction.OnPermissionDenied -> {
                    locationViewModel.launchLocationPermission(false)
                }
            }
        }
    }

    IconButton(onClick = { locationViewModel.launchLocationPermission(true) }) {
        Icon(
            imageVector = Icons.Filled.LocationOn,
            contentDescription = stringResource(id = R.string.icn_permission_icon_description)
        )
    }
}