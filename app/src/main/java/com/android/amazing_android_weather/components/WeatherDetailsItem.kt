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
import com.android.amazing_android_weather.domain.WeatherCityDomain
import com.android.amazing_android_weather.domain.WeatherItemDomain
import com.android.amazing_android_weather.R

@Composable
fun WeatherDetailsItem(modifier: Modifier, city: WeatherCityDomain, item: WeatherItemDomain) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.details_city_label, city.name),
                modifier = Modifier.weight(2f),
            )
            FavIcon(city = city, modifier = Modifier.weight(1f))
        }
        
        Text(
            text = stringResource(id = R.string.details_hour_label, item.hour)
        )

        Image(
            painter = painterResource(
                id = when (item.infos.humidity) {
                    in 0.0..50.0 -> R.drawable.ic_sun
                    in 50.0..80.0 -> R.drawable.ic_cloud
                    in 80.0..90.0 -> R.drawable.ic_cloud_gray
                    else -> R.drawable.ic_rain
                }
            ),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.width(100.dp)
        )

        Text(
            text = stringResource(
                id = R.string.details_infos_label,
                item.infos.temp,
                item.infos.humidity
            )
        )
    }
}
