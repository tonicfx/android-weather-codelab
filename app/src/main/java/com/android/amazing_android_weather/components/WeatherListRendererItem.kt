package com.android.amazing_android_weather.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.amazing_android_weather.domain.WeatherItemDomain
import com.android.amazing_android_weather.R

@Composable
fun WeatherListRendererItem(weatherItem: WeatherItemDomain) {
    Row(
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
            .height(70.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .padding(5.dp)
                .weight(1f)
        ) {
            Text(
                text = stringResource(id = R.string.details_hour_day_label, weatherItem.day, weatherItem.hour)
            )
        }
        Image(
            painter = painterResource(
                id = when (weatherItem.infos.humidity) {
                    in 0.0..50.0 -> R.drawable.ic_sun
                    in 50.0..80.0 -> R.drawable.ic_cloud
                    in 80.0..90.0 -> R.drawable.ic_cloud_gray
                    else -> R.drawable.ic_rain
                }
            ),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.height(70.dp)
        )
        Column(
            modifier = Modifier.padding(5.dp)
        ) {
            Text(
                text = "${weatherItem.infos.temp} °"
            )
            Text(
                text = "${weatherItem.infos.humidity} %"
            )
        }
    }
}