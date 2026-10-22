package sk.styk.martin.apkanalyzer.feature.browse.tv.options

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import sk.styk.martin.apkanalyzer.core.appindex.browse.BrowseDimension
import sk.styk.martin.apkanalyzer.core.appindex.browse.BrowseOption
import sk.styk.martin.apkanalyzer.core.appindex.browse.BrowseSubAttribute
import sk.styk.martin.apkanalyzer.core.appindex.browse.cardLabelRes
import sk.styk.martin.apkanalyzer.core.appindex.browse.isSearchable
import sk.styk.martin.apkanalyzer.core.appindex.browse.titleRes
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.theme.AppTheme
import sk.styk.martin.apkanalyzer.core.designsystem.tv.components.CodeBox
import sk.styk.martin.apkanalyzer.core.designsystem.tv.components.FilterChip
import sk.styk.martin.apkanalyzer.core.designsystem.tv.components.InfoCard
import sk.styk.martin.apkanalyzer.core.designsystem.tv.components.SearchField
import sk.styk.martin.apkanalyzer.core.designsystem.tv.components.Text
import sk.styk.martin.apkanalyzer.core.designsystem.tv.focus.RestorableFocus
import sk.styk.martin.apkanalyzer.core.designsystem.tv.focus.rememberRestorableFocus
import sk.styk.martin.apkanalyzer.core.designsystem.tv.modifier.bleedHorizontally
import sk.styk.martin.apkanalyzer.core.designsystem.tv.theme.ApkAnalyzerTvTheme
import sk.styk.martin.apkanalyzer.feature.browse.tv.bucketIcon
import sk.styk.martin.apkanalyzer.feature.browse.tv.components.BrowseHeader
import sk.styk.martin.apkanalyzer.feature.browse.tv.components.BrowseLoading
import sk.styk.martin.apkanalyzer.feature.browse.tv.icon
import sk.styk.martin.apkanalyzer.core.appindex.R as AppIndexR

@Composable
internal fun TvBrowseOptionsScreen(
    dimension: BrowseDimension,
    onOpenOption: (bucketKey: String, bucketLabel: String, subAttribute: BrowseSubAttribute?) -> Unit,
    viewModel: TvBrowseOptionsViewModel = hiltViewModel { factory: TvBrowseOptionsViewModel.Factory -> factory.create(dimension) },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is TvBrowseOptionsEvent.NavigateToApps -> onOpenOption(event.bucketKey, event.bucketLabel, event.subAttribute)
            }
        }
    }

    TvBrowseOptionsContent(dimension = dimension, state = state, onAction = viewModel::onAction)
}

@Composable
private fun TvBrowseOptionsContent(
    dimension: BrowseDimension,
    state: TvBrowseOptionsState,
    onAction: (TvBrowseOptionsAction) -> Unit,
) {
    when (state) {
        TvBrowseOptionsState.Loading -> BrowseLoading()
        is TvBrowseOptionsState.Loaded -> LoadedContent(dimension = dimension, state = state, onAction = onAction)
    }
}

@Composable
private fun LoadedContent(
    dimension: BrowseDimension,
    state: TvBrowseOptionsState.Loaded,
    onAction: (TvBrowseOptionsAction) -> Unit,
) {
    val focus = rememberRestorableFocus(
        availableKeys = buildSet {
            if (dimension.isSearchable) add(SEARCH_KEY)
            state.subAttributes.forEach { add(subAttributeKey(it)) }
            state.options.forEach { add(optionKey(it)) }
        },
        defaultKey = state.options.firstOrNull()?.let(::optionKey) ?: if (dimension.isSearchable) SEARCH_KEY else "",
    )
    val showsHashes = state.options.any { it is BrowseOption.CertificateHash }

    Column(modifier = Modifier.fillMaxSize().padding(start = 48.dp, end = 48.dp, top = 27.dp)) {
        BrowseHeader(
            icon = dimension.icon,
            title = stringResource(dimension.titleRes),
            subtitle = pluralStringResource(AppIndexR.plurals.browse_dimension_option_count, state.totalOptions, state.totalOptions),
        )
        Controls(dimension = dimension, state = state, focus = focus, onAction = onAction)
        LazyVerticalGrid(
            columns = GridCells.Fixed(if (showsHashes) HASH_COLUMNS else LABEL_COLUMNS),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 16.dp, bottom = 27.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            if (state.options.isEmpty()) {
                item(key = EMPTY_KEY, span = { GridItemSpan(maxLineSpan) }) {
                    Text(
                        text = stringResource(AppIndexR.string.browse_options_empty_query, state.query),
                        style = AppTheme.typography.titleMedium,
                        color = AppTheme.colors.onSurfaceVariant,
                    )
                }
            }
            items(state.options, key = ::optionKey) { option ->
                OptionCard(
                    option = option,
                    icon = dimension.bucketIcon(option.key),
                    onClick = { onAction(TvBrowseOptionsAction.SelectOption(option)) },
                    modifier = focus.modifierFor(optionKey(option)),
                )
            }
        }
    }
}

@Composable
private fun Controls(
    dimension: BrowseDimension,
    state: TvBrowseOptionsState.Loaded,
    focus: RestorableFocus,
    onAction: (TvBrowseOptionsAction) -> Unit,
) {
    if (!dimension.isSearchable && state.subAttributes.isEmpty()) return
    Column(modifier = Modifier.padding(top = 20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        if (dimension.isSearchable) {
            SearchField(
                query = state.query,
                onQueryChange = { onAction(TvBrowseOptionsAction.ChangeQuery(it)) },
                placeholder = pluralStringResource(AppIndexR.plurals.browse_options_filter_hint, state.totalOptions, state.totalOptions),
                modifier = focus.modifierFor(SEARCH_KEY).fillMaxWidth(),
            )
        }
        if (state.subAttributes.isNotEmpty()) {
            LazyRow(
                modifier = Modifier.bleedHorizontally(ChipBleed),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = ChipBleed, vertical = 4.dp),
            ) {
                items(state.subAttributes, key = ::subAttributeKey) { subAttribute ->
                    FilterChip(
                        selected = subAttribute == state.subAttribute,
                        onClick = { onAction(TvBrowseOptionsAction.SelectSubAttribute(subAttribute)) },
                        label = stringResource(subAttribute.labelRes),
                        modifier = focus.modifierFor(subAttributeKey(subAttribute)),
                    )
                }
            }
        }
    }
}

@Composable
private fun OptionCard(
    option: BrowseOption,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (option) {
        is BrowseOption.Labeled -> InfoCard(
            onClick = onClick,
            title = option.label,
            icon = icon,
            modifier = modifier.height(LabeledCardHeight),
        ) {
            option.rawIdentifier?.let { rawIdentifier ->
                Text(
                    text = rawIdentifier,
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colors.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.StartEllipsis,
                )
            }
            AppCount(count = option.count)
        }

        is BrowseOption.CertificateHash -> InfoCard(
            onClick = onClick,
            title = option.publisher ?: stringResource(AppIndexR.string.browse_signer_unknown),
            icon = icon,
            modifier = modifier.height(HashCardHeight),
        ) {
            CodeBox(text = option.label, label = stringResource(option.algorithm.cardLabelRes), maxLines = 3)
            AppCount(count = option.count)
        }
    }
}

@Composable
private fun AppCount(count: Int) {
    Text(
        text = pluralStringResource(AppIndexR.plurals.browse_apps_count, count, count),
        style = AppTheme.typography.labelLarge,
        color = AppTheme.colors.primary,
        modifier = Modifier.padding(top = 4.dp),
    )
}

private fun optionKey(option: BrowseOption) = "option:${option.key}"

private fun subAttributeKey(subAttribute: BrowseSubAttribute) = "attribute:${subAttribute.name}"

private const val SEARCH_KEY = "search"
private const val EMPTY_KEY = "empty"
private const val LABEL_COLUMNS = 3
private const val HASH_COLUMNS = 2
private val ChipBleed = 16.dp
private val LabeledCardHeight = 160.dp
private val HashCardHeight = 228.dp

@Preview(device = Devices.TV_1080p, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun TvBrowseOptionsContentPreview() {
    ApkAnalyzerTvTheme {
        TvBrowseOptionsContent(
            dimension = BrowseDimension.Permission,
            state = TvBrowseOptionsState.Loaded(
                query = "",
                subAttribute = null,
                subAttributes = listOf(),
                totalOptions = 3,
                options = listOf(
                    BrowseOption.Labeled("android.permission.INTERNET", "Full network access", "android.permission.INTERNET", 176),
                    BrowseOption.Labeled("android.permission.CAMERA", "Camera", "android.permission.CAMERA", 24),
                    BrowseOption.Labeled("android.permission.ACCESS_FINE_LOCATION", "Precise location", "android.permission.ACCESS_FINE_LOCATION", 18),
                ),
            ),
            onAction = {},
        )
    }
}

@Preview(device = Devices.TV_1080p, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun TvBrowseOptionsCertificatePreview() {
    ApkAnalyzerTvTheme {
        TvBrowseOptionsContent(
            dimension = BrowseDimension.SigningCertificate,
            state = TvBrowseOptionsState.Loaded(
                query = "",
                subAttribute = BrowseSubAttribute.CertificateSha256,
                subAttributes = BrowseSubAttribute.entries,
                totalOptions = 1,
                options = listOf(
                    BrowseOption.CertificateHash(
                        key = "a1b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1b2",
                        label = "A1:B2:C3:D4:E5:F6:A7:B8:C9:D0:E1:F2:A3:B4:C5:D6:E7:F8:A9:B0:C1:D2:E3:F4:A5:B6:C7:D8:E9:F0:A1:B2",
                        algorithm = BrowseSubAttribute.CertificateSha256,
                        count = 12,
                        publisher = "Google LLC",
                    ),
                ),
            ),
            onAction = {},
        )
    }
}
