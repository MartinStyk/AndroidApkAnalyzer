package sk.styk.martin.apkanalyzer.tv

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import sk.styk.martin.apkanalyzer.core.common.logger.Logger
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.icons.ApkAnalyzerIcons
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.theme.AppTheme
import sk.styk.martin.apkanalyzer.core.designsystem.tv.components.NavigationDrawer
import sk.styk.martin.apkanalyzer.core.designsystem.tv.components.NavigationDrawerItem
import sk.styk.martin.apkanalyzer.core.designsystem.tv.components.ScreenSurface
import sk.styk.martin.apkanalyzer.core.designsystem.tv.components.Text
import sk.styk.martin.apkanalyzer.core.designsystem.tv.theme.ApkAnalyzerTvTheme
import sk.styk.martin.apkanalyzer.core.navigation.Navigator
import sk.styk.martin.apkanalyzer.core.navigation.rememberNavigationState
import sk.styk.martin.apkanalyzer.core.navigation.toEntries
import sk.styk.martin.apkanalyzer.feature.appdetail.tv.navigation.tvAppDetailEntries
import sk.styk.martin.apkanalyzer.feature.apps.api.AppsNavKey
import sk.styk.martin.apkanalyzer.feature.apps.tv.navigation.tvAppEntries
import sk.styk.martin.apkanalyzer.feature.browse.api.BrowseNavKey
import sk.styk.martin.apkanalyzer.feature.apps.api.R as AppsR
import sk.styk.martin.apkanalyzer.feature.browse.api.R as BrowseR

private val TOP_LEVEL_KEYS: List<NavKey> = listOf(AppsNavKey, BrowseNavKey)

@Composable
internal fun TvApkAnalyzerApp(onBackAtRoot: () -> Unit) {
    val navigationState = rememberNavigationState(
        startKey = AppsNavKey,
        topLevelKeys = TOP_LEVEL_KEYS,
    )
    val navigator = remember {
        Navigator(navigationState, onBackAtRoot = onBackAtRoot)
    }

    LaunchedEffect(navigationState.currentKey) {
        Logger.i("Navigation", "Screen opened: ${navigationState.currentKey}")
    }

    val appsTitle = stringResource(AppsR.string.apps)
    val browseTitle = stringResource(BrowseR.string.browse)
    val items = remember(appsTitle, browseTitle) {
        listOf(
            NavigationDrawerItem(navKey = AppsNavKey, icon = ApkAnalyzerIcons.Apps, title = appsTitle),
            NavigationDrawerItem(navKey = BrowseNavKey, icon = ApkAnalyzerIcons.Browse, title = browseTitle),
        )
    }

    ScreenSurface {
        NavigationDrawer(
            items = items,
            selectedKey = navigationState.currentTopLevelKey,
            onSelectKey = navigator::navigate,
        ) {
            NavDisplay(
                entries = navigationState.toEntries(
                    entryProvider {
                        tvAppEntries(navigator)
                        tvAppDetailEntries()
                        entry<BrowseNavKey> { DestinationTitle(title = browseTitle) }
                    },
                ),
                onBack = navigator::goBack,
            )
        }
    }
}

@Composable
private fun DestinationTitle(title: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = title, style = AppTheme.typography.displayMedium)
    }
}

@Preview(device = Devices.TV_1080p)
@Composable
private fun TvApkAnalyzerAppPreview() {
    ApkAnalyzerTvTheme {
        TvApkAnalyzerApp(onBackAtRoot = {})
    }
}
