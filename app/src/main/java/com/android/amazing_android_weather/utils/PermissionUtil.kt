package com.android.amazing_android_weather.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionUtil {
    companion object {
        fun checkIfPermissionGranted(context: Context, permissions: Array<String>): Boolean {
            var granted = true
            permissions.forEach {
                granted = granted && (ContextCompat.checkSelfPermission(context, it)
                        == PackageManager.PERMISSION_GRANTED)
            }

            return granted
        }

        fun shouldShowPermissionRationale(context: Context, permissions: Array<String>): Boolean {
            (context as Activity).let { activity ->
                return permissions.any { permission ->
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        permission
                    )
                }
            }
        }
    }
}