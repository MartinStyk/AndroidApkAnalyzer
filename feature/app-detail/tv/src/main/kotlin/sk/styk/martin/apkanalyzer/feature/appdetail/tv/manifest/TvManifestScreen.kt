package sk.styk.martin.apkanalyzer.feature.appdetail.tv.manifest

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.theme.AppTheme
import sk.styk.martin.apkanalyzer.core.designsystem.tv.components.Text
import sk.styk.martin.apkanalyzer.core.designsystem.tv.theme.ApkAnalyzerTvTheme
import sk.styk.martin.apkanalyzer.feature.appdetail.api.AppDetailInput
import sk.styk.martin.apkanalyzer.feature.appdetail.tv.components.SectionError
import sk.styk.martin.apkanalyzer.feature.appdetail.tv.components.SectionLoading
import sk.styk.martin.apkanalyzer.core.apps.R as AppsR

@Composable
internal fun TvManifestScreen(
    appDetailInput: AppDetailInput,
    viewModel: TvManifestViewModel = hiltViewModel { factory: TvManifestViewModel.Factory ->
        factory.create(appDetailInput)
    },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    TvManifestContent(state = state, onAction = viewModel::onAction)
}

@Composable
private fun TvManifestContent(state: TvManifestState, onAction: (TvManifestAction) -> Unit) {
    when (state) {
        TvManifestState.Loading -> SectionLoading()

        TvManifestState.Error -> SectionError(
            message = stringResource(AppsR.string.manifest_error),
            onRetry = { onAction(TvManifestAction.Retry) },
        )

        is TvManifestState.Loaded -> LoadedContent(state = state)
    }
}

@Composable
private fun LoadedContent(state: TvManifestState.Loaded) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 48.dp, vertical = 27.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(text = stringResource(AppsR.string.manifest_title), style = AppTheme.typography.headlineMedium)
        Text(
            text = pluralStringResource(AppsR.plurals.manifest_line_count, state.lines.size, state.lines.size),
            style = AppTheme.typography.titleMedium,
            color = AppTheme.colors.onSurfaceVariant,
        )
        if (state.additionalInstalledSplits > 0) {
            Text(
                text = pluralStringResource(AppsR.plurals.manifest_base_split_note, state.additionalInstalledSplits, state.additionalInstalledSplits),
                style = AppTheme.typography.bodyLarge,
                color = AppTheme.colors.onSurfaceVariant,
            )
        }
        ManifestCode(lines = state.lines, modifier = Modifier.padding(top = 16.dp))
    }
}

@Composable
private fun ManifestCode(lines: List<String>, modifier: Modifier = Modifier) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }
    val numberWidth = (lines.size.toString().length * NUMBER_CHAR_WIDTH).dp

    LaunchedEffect(Unit) {
        withFrameNanos { }
        focusRequester.requestFocus()
    }

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(20.dp),
        modifier = modifier
            .fillMaxSize()
            .background(color = AppTheme.colors.surface, shape = CodeShape)
            .border(BorderStroke(2.dp, if (isFocused) AppTheme.colors.primary else Color.Transparent), CodeShape)
            .focusRequester(focusRequester)
            .onFocusChanged { isFocused = it.isFocused }
            .onPreviewKeyEvent { event ->
                if (event.type != KeyEventType.KeyDown) return@onPreviewKeyEvent false
                val step = when (event.key) {
                    Key.DirectionDown -> SCROLL_STEP
                    Key.DirectionUp -> -SCROLL_STEP
                    else -> return@onPreviewKeyEvent false
                }
                coroutineScope.launch { listState.animateScrollBy(step) }
                true
            }
            .focusable(),
    ) {
        itemsIndexed(lines) { index, line ->
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = (index + 1).toString(),
                    style = AppTheme.typography.monospace,
                    color = AppTheme.colors.onSurfaceVariant,
                    textAlign = TextAlign.End,
                    modifier = Modifier.width(numberWidth),
                )
                Text(text = line, style = AppTheme.typography.monospace, color = AppTheme.colors.onSurface)
            }
        }
    }
}

private val CodeShape = RoundedCornerShape(16.dp)
private const val SCROLL_STEP = 360f
private const val NUMBER_CHAR_WIDTH = 12

@Preview(device = Devices.TV_1080p, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun TvManifestContentPreview() {
    ApkAnalyzerTvTheme {
        TvManifestContent(
            state = TvManifestState.Loaded(
                lines = listOf(
                    "<manifest package=\"com.google.android.youtube.tv\">",
                    "    <uses-permission android:name=\"android.permission.INTERNET\" />",
                    "    <application android:label=\"YouTube\">",
                    "    </application>",
                    "</manifest>",
                ),
                additionalInstalledSplits = 2,
            ),
            onAction = {},
        )
    }
}
