package com.android.amazing_android_weather.viewmodel

import android.location.Location
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.TimeUnit

data class LocationrViewModelState(
    var location: Location? = null,
    var isLocationSearching: Boolean = false,
)

class LocationViewModel(private val client: FusedLocationProviderClient? = null) : ViewModel() {

    companion object {
        private const val UPDATE_INTERVAL_SECS = 10L
        private const val FASTEST_UPDATE_INTERVAL_SECS = 2L
    }

    var askLocationPermission = MutableStateFlow(false)
        private set

    fun launchLocationPermission(request: Boolean) {
        askLocationPermission.value = request
    }

    fun locate() = callbackFlow<LocationrViewModelState> {
        val locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(UPDATE_INTERVAL_SECS)
            fastestInterval = TimeUnit.SECONDS.toMillis(FASTEST_UPDATE_INTERVAL_SECS)
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }

        val callBack = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val location = locationResult.lastLocation

                Toast.makeText(
                    client?.applicationContext,
                    "Get location $location.",
                    Toast.LENGTH_SHORT
                ).show()

                trySend(LocationrViewModelState(location, false))
            }
        }

        trySend(LocationrViewModelState(null, true))
        client?.requestLocationUpdates(locationRequest, callBack, Looper.getMainLooper())

        awaitClose { client?.removeLocationUpdates(callBack) }
    }
}