package sk.styk.martin.apkanalyzer.feature.apps.tv.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import sk.styk.martin.apkanalyzer.core.common.model.PackageName
import sk.styk.martin.apkanalyzer.core.common.model.megabytes
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.theme.AppTheme
import sk.styk.martin.apkanalyzer.core.designsystem.tv.components.AppCard
import sk.styk.martin.apkanalyzer.core.designsystem.tv.components.AppCardWidth
import sk.styk.martin.apkanalyzer.core.designsystem.tv.components.Button
import sk.styk.martin.apkanalyzer.core.designsystem.tv.components.SearchField
import sk.styk.martin.apkanalyzer.core.designsystem.tv.components.Text
import sk.styk.martin.apkanalyzer.core.designsystem.tv.focus.RestorableFocus
import sk.styk.martin.apkanalyzer.core.designsystem.tv.focus.rememberRestorableFocus
import sk.styk.martin.apkanalyzer.core.designsystem.tv.theme.ApkAnalyzerTvTheme
import sk.styk.martin.apkanalyzer.feature.apps.tv.R
import sk.styk.martin.apkanalyzer.feature.apps.tv.components.TvAppItem
import java.time.Instant

@Composable
internal fun TvAppSearchScreen(onOpenAppDetail: (PackageName) -> Unit, viewModel: TvAppSearchViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is TvAppSearchEvent.NavigateToAppDetail -> onOpenAppDetail(event.packageName)
            }
        }
    }

    TvAppSearchContent(state = state, onAction = viewModel::onAction)
}

@Composable
private fun TvAppSearchContent(state: TvAppSearchState, onAction: (TvAppSearchAction) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val firstResultFocusRequester = remember { FocusRequester() }
    val focus = rememberRestorableFocus(
        availableKeys = buildSet {
            add(FIELD_KEY)
            if (state.query.isBlank()) {
                state.history.forEach { add(historyKey(it.app.packageName)) }
            } else {
                state.results.forEach { add(resultKey(it.packageName)) }
            }
        },
        defaultKey = FIELD_KEY,
        onFirstFocus = { keyboardController?.show() },
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 27.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SearchField(
            query = state.query,
            onQueryChange = { onAction(TvAppSearchAction.QueryChanged(it)) },
            placeholder = stringResource(R.string.tv_search_placeholder),
            modifier = Modifier
                .padding(horizontal = 48.dp)
                .fillMaxWidth()
                .then(focus.modifierFor(FIELD_KEY)),
            onSearch = {
                if (state.query.isNotBlank() && state.results.isNotEmpty()) firstResultFocusRequester.requestFocus()
            },
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = AppCardWidth),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 48.dp, end = 48.dp, top = 8.dp, bottom = 27.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            if (state.query.isBlank()) {
                historySection(history = state.history, focus = focus, onAction = onAction)
            } else {
                resultsSection(
                    query = state.query,
                    results = state.results,
                    focus = focus,
                    firstResultFocusRequester = firstResultFocusRequester,
                    onAction = onAction,
                )
            }
        }
    }
}

private fun LazyGridScope.historySection(
    history: List<TvSearchHistoryItem>,
    focus: RestorableFocus,
    onAction: (TvAppSearchAction) -> Unit,
) {
    if (history.isEmpty()) return
    item(key = HISTORY_HEADER_KEY, span = { GridItemSpan(maxLineSpan) }) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            SectionTitle(text = stringResource(R.string.tv_search_history))
            Button(
                onClick = { onAction(TvAppSearchAction.ClearHistory) },
                label = stringResource(R.string.tv_search_clear_history),
            )
        }
    }
    items(items = history, key = { historyKey(it.app.packageName) }) { item ->
        AppCard(
            packageName = item.app.packageName,
            applicationName = item.app.applicationName,
            onClick = { onAction(TvAppSearchAction.HistoryItemClicked(item.app.packageName)) },
            description = stringResource(R.string.tv_search_history_query, item.query),
            modifier = focus.modifierFor(historyKey(item.app.packageName)),
        )
    }
}

private fun LazyGridScope.resultsSection(
    query: String,
    results: List<TvAppItem>,
    focus: RestorableFocus,
    firstResultFocusRequester: FocusRequester,
    onAction: (TvAppSearchAction) -> Unit,
) {
    if (results.isEmpty()) {
        item(key = NO_RESULTS_KEY, span = { GridItemSpan(maxLineSpan) }) {
            Box(modifier = Modifier.padding(top = 24.dp)) {
                Text(
                    text = stringResource(R.string.tv_search_no_results, query.trim()),
                    style = AppTheme.typography.titleMedium,
                    color = AppTheme.colors.onSurfaceVariant,
                )
            }
        }
        return
    }
    itemsIndexed(items = results, key = { _, app -> resultKey(app.packageName) }) { index, app ->
        AppCard(
            packageName = app.packageName,
            applicationName = app.applicationName,
            onClick = { onAction(TvAppSearchAction.ResultClicked(app.packageName)) },
            modifier = focus.modifierFor(resultKey(app.packageName))
                .then(if (index == 0) Modifier.focusRequester(firstResultFocusRequester) else Modifier),
        )
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text = text, style = AppTheme.typography.titleLarge)
}

private fun historyKey(packageName: PackageName) = "history:${packageName.value}"

private fun resultKey(packageName: PackageName) = "result:${packageName.value}"

private const val FIELD_KEY = "field"
private const val HISTORY_HEADER_KEY = "history-header"
private const val NO_RESULTS_KEY = "no-results"

private val previewApp = TvAppItem(
    packageName = PackageName("com.google.android.youtube.tv"),
    applicationName = "YouTube",
    targetSdk = 34,
    apkSize = 142.megabytes,
    totalSize = null,
    installTime = Instant.parse("2025-03-02T10:00:00Z"),
    lastUpdateTime = Instant.parse("2026-09-14T10:00:00Z"),
    lastUsedTime = null,
)

@Preview(device = Devices.TV_1080p, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun TvAppSearchHistoryPreview() {
    ApkAnalyzerTvTheme {
        TvAppSearchContent(
            state = TvAppSearchState(history = listOf(TvSearchHistoryItem(app = previewApp, query = "yt"))),
            onAction = {},
        )
    }
}

@Preview(device = Devices.TV_1080p, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun TvAppSearchResultsPreview() {
    ApkAnalyzerTvTheme {
        TvAppSearchContent(
            state = TvAppSearchState(query = "you", results = listOf(previewApp)),
            onAction = {},
        )
    }
}
