package com.android.amazing_android_weather.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.android.amazing_android_weather.R

@Composable
fun SplashScreen(navHostController: NavHostController) {
    val raw = R.raw.splash_animation
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(raw))
    val logoAnimationState =
        animateLottieCompositionAsState(composition = composition)

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            composition = composition,
            progress = { logoAnimationState.progress }
        )
    }

    if (logoAnimationState.isAtEnd && logoAnimationState.isPlaying) {
        navHostController.navigate(Screen.WeatherSearch.route) {
            // suppression de la backstack
            popUpTo(Screen.WeatherSplash.route) {
                inclusive = true
            }
        }
    }
}