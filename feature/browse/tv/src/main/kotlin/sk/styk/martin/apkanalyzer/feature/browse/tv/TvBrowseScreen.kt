package sk.styk.martin.apkanalyzer.feature.browse.tv

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import sk.styk.martin.apkanalyzer.core.appindex.browse.BrowseDimension
import sk.styk.martin.apkanalyzer.core.appindex.browse.BrowseDimensionSummary
import sk.styk.martin.apkanalyzer.core.appindex.browse.subtitleRes
import sk.styk.martin.apkanalyzer.core.appindex.browse.titleRes
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.theme.AppTheme
import sk.styk.martin.apkanalyzer.core.designsystem.tv.components.InfoCard
import sk.styk.martin.apkanalyzer.core.designsystem.tv.components.Text
import sk.styk.martin.apkanalyzer.core.designsystem.tv.focus.rememberRestorableFocus
import sk.styk.martin.apkanalyzer.core.designsystem.tv.theme.ApkAnalyzerTvTheme
import sk.styk.martin.apkanalyzer.feature.browse.tv.components.BrowseLoading
import sk.styk.martin.apkanalyzer.core.appindex.R as AppIndexR
import sk.styk.martin.apkanalyzer.feature.browse.api.R as BrowseApiR

@Composable
internal fun TvBrowseScreen(onOpenDimension: (BrowseDimension) -> Unit, viewModel: TvBrowseViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is TvBrowseEvent.NavigateToDimension -> onOpenDimension(event.dimension)
            }
        }
    }

    TvBrowseContent(state = state, onAction = viewModel::onAction)
}

@Composable
private fun TvBrowseContent(state: TvBrowseState, onAction: (TvBrowseAction) -> Unit) {
    when (state) {
        TvBrowseState.Loading -> BrowseLoading()
        is TvBrowseState.Loaded -> LoadedContent(state = state, onAction = onAction)
    }
}

@Composable
private fun LoadedContent(state: TvBrowseState.Loaded, onAction: (TvBrowseAction) -> Unit) {
    val focus = rememberRestorableFocus(
        availableKeys = state.dimensions.mapTo(mutableSetOf()) { it.dimension.name },
        defaultKey = state.dimensions.firstOrNull()?.dimension?.name.orEmpty(),
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(COLUMNS),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 48.dp, vertical = 32.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        item(key = HEADER_KEY, span = { GridItemSpan(maxLineSpan) }) {
            Column(modifier = Modifier.padding(bottom = 8.dp)) {
                Text(text = stringResource(BrowseApiR.string.browse), style = AppTheme.typography.headlineSmall)
                Text(
                    text = pluralStringResource(AppIndexR.plurals.browse_subtitle, state.totalApps, state.totalApps),
                    style = AppTheme.typography.titleMedium,
                    color = AppTheme.colors.onSurfaceVariant,
                )
            }
        }
        items(state.dimensions, key = { it.dimension.name }) { summary ->
            DimensionCard(
                summary = summary,
                onClick = { onAction(TvBrowseAction.SelectDimension(summary.dimension)) },
                modifier = focus.modifierFor(summary.dimension.name).height(CardHeight),
            )
        }
    }
}

@Composable
private fun DimensionCard(
    summary: BrowseDimensionSummary,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    InfoCard(
        onClick = onClick,
        title = stringResource(summary.dimension.titleRes),
        icon = summary.dimension.icon,
        modifier = modifier,
    ) {
        Text(
            text = stringResource(summary.dimension.subtitleRes),
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colors.onSurfaceVariant,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        if (summary.topLabels.isNotEmpty()) {
            val resources = LocalResources.current
            Text(
                text = summary.topLabels.reduce { joined, label -> resources.getString(R.string.tv_browse_label_list, joined, label) },
                style = AppTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
        Text(
            text = pluralStringResource(AppIndexR.plurals.browse_dimension_option_count, summary.optionCount, summary.optionCount),
            style = AppTheme.typography.labelLarge,
            color = AppTheme.colors.primary,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

private const val COLUMNS = 3
private const val HEADER_KEY = "header"
private val CardHeight = 228.dp

@Preview(device = Devices.TV_1080p, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun TvBrowseContentPreview() {
    ApkAnalyzerTvTheme {
        TvBrowseContent(
            state = TvBrowseState.Loaded(
                totalApps = 142,
                dimensions = listOf(
                    BrowseDimensionSummary(BrowseDimension.Permission, 186, listOf("Full network access", "Run at startup", "Camera")),
                    BrowseDimensionSummary(BrowseDimension.SigningCertificate, 24, listOf("Google LLC", "Android", "Netflix")),
                    BrowseDimensionSummary(BrowseDimension.TargetSdk, 9, listOf("Android 15", "Android 14", "Android 13")),
                    BrowseDimensionSummary(BrowseDimension.InstallSource, 3, listOf("Google Play", "System", "Sideloaded")),
                ),
            ),
            onAction = {},
        )
    }
}
