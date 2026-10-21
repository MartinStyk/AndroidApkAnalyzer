package sk.styk.martin.apkanalyzer.feature.appdetail.tv

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import sk.styk.martin.apkanalyzer.core.common.model.AppReference
import sk.styk.martin.apkanalyzer.core.common.model.PackageName
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.icons.AppIcon
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.theme.AppTheme
import sk.styk.martin.apkanalyzer.core.designsystem.tv.components.Text
import sk.styk.martin.apkanalyzer.core.designsystem.tv.theme.ApkAnalyzerTvTheme
import sk.styk.martin.apkanalyzer.feature.appdetail.api.AppDetailInput

@Composable
internal fun TvAppDetailScreen(
    appDetailInput: AppDetailInput,
    viewModel: TvAppDetailViewModel = hiltViewModel { factory: TvAppDetailViewModel.Factory ->
        factory.create(appDetailInput)
    },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    TvAppDetailContent(state = state)
}

@Composable
private fun TvAppDetailContent(state: TvAppDetailState) {
    Box(modifier = Modifier.fillMaxSize().padding(48.dp), contentAlignment = Alignment.CenterStart) {
        when (state) {
            TvAppDetailState.Loading -> Text(
                text = stringResource(R.string.tv_app_detail_loading),
                color = AppTheme.colors.onSurfaceVariant,
            )

            TvAppDetailState.Error -> Text(
                text = stringResource(R.string.tv_app_detail_error),
                color = AppTheme.colors.onSurfaceVariant,
            )

            is TvAppDetailState.Loaded -> Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(32.dp),
            ) {
                AppIcon(source = state.appReference, size = 96.dp)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = state.appName, style = AppTheme.typography.displaySmall)
                    Text(text = state.packageName, style = AppTheme.typography.titleMedium, color = AppTheme.colors.onSurfaceVariant)
                    state.versionName?.let { versionName ->
                        Text(
                            text = stringResource(R.string.tv_app_detail_version, versionName),
                            style = AppTheme.typography.bodyLarge,
                            color = AppTheme.colors.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}

@Preview(device = Devices.TV_1080p, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun TvAppDetailContentPreview() {
    ApkAnalyzerTvTheme {
        TvAppDetailContent(
            state = TvAppDetailState.Loaded(
                appReference = AppReference.InstalledPackage(PackageName("com.google.android.youtube.tv")),
                appName = "YouTube",
                packageName = "com.google.android.youtube.tv",
                versionName = "4.40.303",
            ),
        )
    }
}
