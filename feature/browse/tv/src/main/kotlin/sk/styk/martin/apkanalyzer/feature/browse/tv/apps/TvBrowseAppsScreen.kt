package sk.styk.martin.apkanalyzer.feature.browse.tv.apps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import sk.styk.martin.apkanalyzer.core.appindex.browse.BrowseBucketDetail
import sk.styk.martin.apkanalyzer.core.appindex.browse.BrowseDimension
import sk.styk.martin.apkanalyzer.core.appindex.browse.BrowseSubAttribute
import sk.styk.martin.apkanalyzer.core.appindex.browse.cardLabelRes
import sk.styk.martin.apkanalyzer.core.appindex.browse.isCertificateHash
import sk.styk.martin.apkanalyzer.core.apppermissions.detail.explanationRes
import sk.styk.martin.apkanalyzer.core.apppermissions.detail.labelRes
import sk.styk.martin.apkanalyzer.core.apps.permissions.ProtectionLevel
import sk.styk.martin.apkanalyzer.core.apps.signing.CertificateValidity
import sk.styk.martin.apkanalyzer.core.apps.signing.displayLine
import sk.styk.martin.apkanalyzer.core.apps.signing.explanationRes
import sk.styk.martin.apkanalyzer.core.apps.signing.isWeak
import sk.styk.martin.apkanalyzer.core.apps.signing.labelRes
import sk.styk.martin.apkanalyzer.core.common.model.PackageName
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.icons.ApkAnalyzerIcons
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.theme.AppTheme
import sk.styk.martin.apkanalyzer.core.designsystem.tv.components.AppCard
import sk.styk.martin.apkanalyzer.core.designsystem.tv.components.AppCardWidth
import sk.styk.martin.apkanalyzer.core.designsystem.tv.components.CodeBox
import sk.styk.martin.apkanalyzer.core.designsystem.tv.components.ScrollablePanel
import sk.styk.martin.apkanalyzer.core.designsystem.tv.components.SearchField
import sk.styk.martin.apkanalyzer.core.designsystem.tv.components.Tag
import sk.styk.martin.apkanalyzer.core.designsystem.tv.components.Text
import sk.styk.martin.apkanalyzer.core.designsystem.tv.focus.rememberRestorableFocus
import sk.styk.martin.apkanalyzer.core.designsystem.tv.focus.restorableFocus
import sk.styk.martin.apkanalyzer.core.designsystem.tv.theme.ApkAnalyzerTvTheme
import sk.styk.martin.apkanalyzer.feature.browse.tv.R
import sk.styk.martin.apkanalyzer.feature.browse.tv.bucketIcon
import sk.styk.martin.apkanalyzer.feature.browse.tv.components.BrowseHeader
import sk.styk.martin.apkanalyzer.feature.browse.tv.components.BrowseLoading
import java.text.DateFormat
import java.util.Date
import sk.styk.martin.apkanalyzer.core.appindex.R as AppIndexR
import sk.styk.martin.apkanalyzer.core.apps.R as AppsR

@Composable
internal fun TvBrowseAppsScreen(
    dimension: BrowseDimension,
    bucketKey: String,
    bucketLabel: String,
    subAttribute: BrowseSubAttribute?,
    onOpenApp: (PackageName) -> Unit,
    viewModel: TvBrowseAppsViewModel = hiltViewModel { factory: TvBrowseAppsViewModel.Factory ->
        factory.create(dimension, bucketKey, subAttribute)
    },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is TvBrowseAppsEvent.NavigateToAppDetail -> onOpenApp(event.packageName)
            }
        }
    }

    TvBrowseAppsContent(
        dimension = dimension,
        bucketKey = bucketKey,
        bucketLabel = bucketLabel,
        subAttribute = subAttribute,
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun TvBrowseAppsContent(
    dimension: BrowseDimension,
    bucketKey: String,
    bucketLabel: String,
    subAttribute: BrowseSubAttribute?,
    state: TvBrowseAppsState,
    onAction: (TvBrowseAppsAction) -> Unit,
) {
    when (state) {
        TvBrowseAppsState.Loading -> BrowseLoading()

        is TvBrowseAppsState.Loaded -> LoadedContent(
            dimension = dimension,
            bucketKey = bucketKey,
            bucketLabel = bucketLabel,
            subAttribute = subAttribute,
            state = state,
            onAction = onAction,
        )
    }
}

@Composable
private fun LoadedContent(
    dimension: BrowseDimension,
    bucketKey: String,
    bucketLabel: String,
    subAttribute: BrowseSubAttribute?,
    state: TvBrowseAppsState.Loaded,
    onAction: (TvBrowseAppsAction) -> Unit,
) {
    val focus = rememberRestorableFocus(
        availableKeys = buildSet {
            add(SEARCH_KEY)
            state.apps.forEach { add(it.packageName.value) }
        },
        defaultKey = state.apps.firstOrNull()?.packageName?.value ?: SEARCH_KEY,
    )
    val isHashBucket = subAttribute.isCertificateHash

    Column(modifier = Modifier.fillMaxSize().padding(start = 48.dp, end = 48.dp, top = 27.dp)) {
        BrowseHeader(
            icon = dimension.bucketIcon(bucketKey),
            title = when {
                !isHashBucket -> bucketLabel

                else -> (state.bucketDetail as? BrowseBucketDetail.Certificate)?.subject?.displayName
                    ?: stringResource(AppIndexR.string.browse_signer_unknown)
            },
            subtitle = pluralStringResource(AppIndexR.plurals.browse_apps_count, state.totalApps, state.totalApps),
        )
        Row(modifier = Modifier.padding(top = 20.dp, bottom = 27.dp), horizontalArrangement = Arrangement.spacedBy(32.dp)) {
            if (state.bucketDetail != null || isHashBucket) {
                BucketInfo(
                    detail = state.bucketDetail,
                    fingerprint = bucketLabel.takeIf { isHashBucket },
                    fingerprintLabel = subAttribute?.let { stringResource(it.cardLabelRes) },
                    modifier = Modifier.width(InfoWidth),
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                SearchField(
                    query = state.query,
                    onQueryChange = { onAction(TvBrowseAppsAction.ChangeQuery(it)) },
                    placeholder = pluralStringResource(AppIndexR.plurals.browse_apps_filter_hint, state.totalApps, state.totalApps),
                    modifier = Modifier.restorableFocus(focus, SEARCH_KEY).fillMaxWidth(),
                )
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = AppCardWidth),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 20.dp, bottom = 27.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    if (state.apps.isEmpty()) {
                        item(key = EMPTY_KEY, span = { GridItemSpan(maxLineSpan) }) {
                            Text(
                                text = if (state.query.isBlank()) {
                                    stringResource(R.string.tv_browse_no_apps)
                                } else {
                                    stringResource(AppIndexR.string.browse_apps_empty_query, state.query)
                                },
                                style = AppTheme.typography.titleMedium,
                                color = AppTheme.colors.onSurfaceVariant,
                            )
                        }
                    }
                    items(state.apps, key = { it.packageName.value }) { app ->
                        AppCard(
                            packageName = app.packageName,
                            applicationName = app.applicationName,
                            onClick = { onAction(TvBrowseAppsAction.SelectApp(app.packageName)) },
                            modifier = Modifier.restorableFocus(focus, app.packageName.value),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BucketInfo(
    detail: BrowseBucketDetail?,
    fingerprint: String?,
    fingerprintLabel: String?,
    modifier: Modifier = Modifier,
) {
    ScrollablePanel(modifier = modifier, contentPadding = PaddingValues(20.dp), spacing = 12.dp) {
        Text(
            text = stringResource(
                if (detail is BrowseBucketDetail.Permission) R.string.tv_browse_permission_about else R.string.tv_browse_certificate_about,
            ),
            style = AppTheme.typography.titleMedium,
        )
        fingerprint?.let { CodeBox(text = it, label = fingerprintLabel, modifier = Modifier.fillMaxWidth()) }
        when (detail) {
            is BrowseBucketDetail.Permission -> PermissionInfo(detail)
            is BrowseBucketDetail.Certificate -> CertificateInfo(detail)
            null -> Unit
        }
    }
}

@Composable
private fun PermissionInfo(detail: BrowseBucketDetail.Permission) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Tag(
            label = stringResource(detail.protectionLevel.labelRes),
            icon = if (detail.protectionLevel == ProtectionLevel.Dangerous) ApkAnalyzerIcons.Warning else ApkAnalyzerIcons.Permissions,
        )
        InfoText(text = stringResource(detail.protectionLevel.explanationRes))
        detail.description?.let { InfoText(text = it) }
    }
}

@Composable
private fun CertificateInfo(detail: BrowseBucketDetail.Certificate) {
    val dateFormat = remember { DateFormat.getDateInstance(DateFormat.MEDIUM) }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Tag(
                label = stringResource(detail.validity.labelRes),
                icon = if (detail.validity == CertificateValidity.Valid) ApkAnalyzerIcons.Calendar else ApkAnalyzerIcons.Warning,
            )
            Tag(
                label = stringResource(detail.algorithmAssessment.labelRes),
                icon = if (detail.algorithmAssessment.isWeak) ApkAnalyzerIcons.Warning else ApkAnalyzerIcons.VerifiedShield,
            )
            if (detail.isSelfSigned) {
                Tag(label = stringResource(AppIndexR.string.browse_certificate_self_signed))
            }
        }
        detail.subject.organization?.let { InfoField(label = stringResource(AppsR.string.certificates_organization), value = it) }
        detail.subject.name?.takeIf { it != detail.subject.organization }?.let { InfoField(label = stringResource(AppsR.string.certificates_name), value = it) }
        detail.subject.country?.let { InfoField(label = stringResource(AppsR.string.certificates_country), value = it) }
        if (!detail.isSelfSigned) {
            detail.issuer.displayLine?.let { InfoField(label = stringResource(AppsR.string.certificates_issuer), value = it) }
        }
        InfoField(
            label = stringResource(AppsR.string.certificates_validity),
            value = stringResource(
                AppsR.string.certificates_date_range,
                dateFormat.format(Date.from(detail.validFrom)),
                dateFormat.format(Date.from(detail.validUntil)),
            ),
        )
        if (detail.validity != CertificateValidity.Valid) {
            InfoText(text = stringResource(detail.validity.explanationRes))
        }
        InfoText(text = stringResource(detail.algorithmAssessment.explanationRes, detail.algorithm))
        if (detail.isSelfSigned) {
            InfoText(text = stringResource(AppIndexR.string.browse_certificate_self_signed_explanation))
        }
    }
}

@Composable
private fun InfoField(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(text = label, style = AppTheme.typography.labelMedium, color = AppTheme.colors.onSurfaceVariant)
        Text(text = value, style = AppTheme.typography.bodyLarge)
    }
}

@Composable
private fun InfoText(text: String) {
    Text(text = text, style = AppTheme.typography.bodyMedium, color = AppTheme.colors.onSurfaceVariant)
}

private const val SEARCH_KEY = "search"
private const val EMPTY_KEY = "empty"
private val InfoWidth = 300.dp

@Preview(device = Devices.TV_1080p, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun TvBrowseAppsContentPreview() {
    ApkAnalyzerTvTheme {
        TvBrowseAppsContent(
            dimension = BrowseDimension.Permission,
            bucketKey = "android.permission.CAMERA",
            bucketLabel = "Camera",
            subAttribute = null,
            state = TvBrowseAppsState.Loaded(
                query = "",
                totalApps = 2,
                apps = listOf(
                    TvBrowseAppItem(PackageName("com.google.android.youtube.tv"), "YouTube"),
                    TvBrowseAppItem(PackageName("com.netflix.ninja"), "Netflix"),
                ),
                bucketDetail = BrowseBucketDetail.Permission(
                    protectionLevel = ProtectionLevel.Dangerous,
                    description = "Allows the app to take pictures and videos with any camera.",
                ),
            ),
            onAction = {},
        )
    }
}
