package sk.styk.martin.apkanalyzer.core.designsystem.tv.focus

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
class RestorableFocus internal constructor(
    internal val restoreKey: String,
    internal val restoreFocusRequester: FocusRequester,
    internal val onFocusKey: (String) -> Unit,
)

fun Modifier.restorableFocus(focus: RestorableFocus, key: String): Modifier = this
    .then(if (key == focus.restoreKey) Modifier.focusRequester(focus.restoreFocusRequester) else Modifier)
    .onFocusChanged { if (it.isFocused) focus.onFocusKey(key) }

@Composable
fun rememberRestorableFocus(
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
