package com.android.amazing_android_weather.components

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarResult
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import com.android.amazing_android_weather.utils.PermissionUtil

sealed class PermissionAction {
    object OnPermissionGranted : PermissionAction()
    object OnPermissionDenied : PermissionAction()
}

@Composable
fun PermissionPopupSystem(
    context: Context,
    permissions: Array<String>,
    permissionRationale: String,
    permissionAction: (PermissionAction) -> Unit
) {

    val scaffoldState = rememberScaffoldState()
    val permissionGranted =
        PermissionUtil.checkIfPermissionGranted(
            context,
            permissions
        )

    if (permissionGranted) {
        permissionAction(PermissionAction.OnPermissionGranted)
        return
    }


    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsResult ->
        val isGranted = permissions.any {
            permissionsResult.containsKey(it) && permissionsResult.getOrDefault(it, false)
        }

        if (isGranted) {
            // Permission Accepted
            permissionAction(PermissionAction.OnPermissionGranted)
        } else {
            // Permission Denied
            permissionAction(PermissionAction.OnPermissionDenied)
        }
    }

    val showPermissionRationale = PermissionUtil.shouldShowPermissionRationale(
        context,
        permissions
    )

    if (showPermissionRationale) {
        LaunchedEffect(showPermissionRationale) {
            val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                message = permissionRationale,
                actionLabel = "Grant Access",
                duration = SnackbarDuration.Long
            )
            when (snackbarResult) {
                SnackbarResult.Dismissed -> {
                    //User denied the permission, do nothing
                    permissionAction(PermissionAction.OnPermissionDenied)
                }
                SnackbarResult.ActionPerformed -> {
                    launcher.launch(permissions)
                }
            }
        }
    } else {
        // https://developer.android.com/jetpack/compose/side-effects
        SideEffect {
            launcher.launch(permissions)
        }
    }
}