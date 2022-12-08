package com.android.amazing_android_weather.components

import android.location.Location
import android.view.KeyEvent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.android.amazing_android_weather.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WeatherSearchBar(
    searchText: String,
    placeholderText: String = "",
    onSearchTextChanged: (String) -> Unit = {},
    onClearClick: () -> Unit = {},
    onLocateSearching: (value: Boolean) -> Unit = {},
    onLocateChange: (location: Location) -> Unit = {}
) {
    var showClearButton by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                showClearButton = (focusState.isFocused)
            }
            .onKeyEvent {
                if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                    keyboardController?.hide()
                    false
                }
                true
            },
        value = searchText,
        onValueChange = onSearchTextChanged,
        placeholder = {
            Text(text = placeholderText)
        },
        trailingIcon = {

            Row {
                AnimatedVisibility(visible = showClearButton) {
                    IconButton(
                        onClick = { onClearClick() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(id = R.string.icn_search_clear_content_description)
                        )
                    }
                }
                LocationPermissionIcon(
                    locationChange = onLocateChange,
                    locationSearching = onLocateSearching
                )
            }
        },
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
            },
            onGo = {
                keyboardController?.hide()
            },
            onNext = {
                keyboardController?.hide()
            },
        ),
    )
}