package sk.styk.martin.apkanalyzer.feature.browse.tv.components

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.icons.ApkAnalyzerIcons
import sk.styk.martin.apkanalyzer.core.designsystem.tv.components.Button
import sk.styk.martin.apkanalyzer.core.designsystem.tv.components.SearchField
import sk.styk.martin.apkanalyzer.core.designsystem.tv.theme.ApkAnalyzerTvTheme
import sk.styk.martin.apkanalyzer.feature.browse.tv.R

@Composable
internal fun BrowseSearch(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
) {
    var opened by remember { mutableStateOf(false) }

    if (opened || query.isNotEmpty()) {
        OpenedSearch(
            query = query,
            onQueryChange = onQueryChange,
            placeholder = placeholder,
            focusOnOpen = opened,
            onClose = { opened = false },
            modifier = modifier,
        )
    } else {
        Button(
            onClick = { opened = true },
            label = stringResource(R.string.tv_browse_search),
            leadingIcon = ApkAnalyzerIcons.Search,
            modifier = modifier,
        )
    }
}

@Composable
private fun OpenedSearch(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String,
    focusOnOpen: Boolean,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    var wasFocused by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (focusOnOpen) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    SearchField(
        query = query,
        onQueryChange = onQueryChange,
        placeholder = placeholder,
        modifier = modifier
            .width(SearchWidth)
            .focusRequester(focusRequester)
            .onFocusChanged { state ->
                when {
                    state.isFocused -> wasFocused = true
                    wasFocused && query.isEmpty() -> onClose()
                }
            },
    )
}

private val SearchWidth = 360.dp

@Preview(device = Devices.TV_1080p, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun BrowseSearchPreview() {
    ApkAnalyzerTvTheme {
        BrowseSearch(query = "camera", onQueryChange = {}, placeholder = "Filter 186 options")
    }
}
