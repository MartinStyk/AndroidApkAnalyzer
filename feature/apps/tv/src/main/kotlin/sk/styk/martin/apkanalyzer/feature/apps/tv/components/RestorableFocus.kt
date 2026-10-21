package sk.styk.martin.apkanalyzer.feature.apps.tv.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged

@Stable
internal class RestorableFocus(
    private val restoreKey: String,
    private val restoreFocusRequester: FocusRequester,
    private val onFocusKey: (String) -> Unit,
) {
    fun modifierFor(key: String): Modifier = Modifier
        .then(if (key == restoreKey) Modifier.focusRequester(restoreFocusRequester) else Modifier)
        .onFocusChanged { if (it.isFocused) onFocusKey(key) }
}

@Composable
internal fun rememberRestorableFocus(
    availableKeys: Set<String>,
    defaultKey: String,
    onFirstFocus: () -> Unit = {},
): RestorableFocus {
    var focusedKey by rememberSaveable { mutableStateOf<String?>(null) }
    val restoreFocusRequester = remember { FocusRequester() }
    val restoreKey = focusedKey?.takeIf { it in availableKeys } ?: defaultKey
    var focusRestored by remember { mutableStateOf(false) }
    var firstFocusHandled by remember { mutableStateOf(false) }

    LaunchedEffect(restoreKey) {
        if (focusRestored) return@LaunchedEffect
        withFrameNanos { }
        restoreFocusRequester.requestFocus()
        if (!firstFocusHandled && focusedKey == null) {
            firstFocusHandled = true
            onFirstFocus()
        }
    }

    return RestorableFocus(
        restoreKey = restoreKey,
        restoreFocusRequester = restoreFocusRequester,
        onFocusKey = {
            focusRestored = true
            focusedKey = it
        },
    )
}
