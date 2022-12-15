package com.android.amazing_android_weather.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.amazing_android_weather.R
import com.android.amazing_android_weather.viewmodel.FavViewModel
import com.android.amazing_android_weather.viewmodel.FavViewModelState
import org.koin.androidx.compose.getViewModel

@Composable
fun FavsList(modifier: Modifier, selectionChange: (String) -> Unit) {
    val favViewModel = getViewModel<FavViewModel>()

    val state by remember(favViewModel) {
        favViewModel.load()
    }.collectAsState(initial = FavViewModelState())

    Column(modifier = modifier) {
        if (state.isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                CircularProgressIndicator()
            }
        } else if (state.favs.isNotEmpty()) {
            Row(
                modifier = modifier.padding(1.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    modifier = Modifier.padding(10.dp),
                    imageVector = Icons.Filled.List,
                    contentDescription = stringResource(id = R.string.icn_go_to_details)
                )
                Text(
                    text = stringResource(id = R.string.fav_title_label),
                    style = MaterialTheme.typography.h6
                )
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                items(items = state.favs, itemContent = { item ->
                    Text("- ${item.name}", modifier = Modifier
                        .padding(5.dp)
                        .clickable {
                            selectionChange(item.name)
                        })
                })
            }
        }
    }
}