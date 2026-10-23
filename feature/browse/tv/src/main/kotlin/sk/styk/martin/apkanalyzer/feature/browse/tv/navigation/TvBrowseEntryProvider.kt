package sk.styk.martin.apkanalyzer.feature.browse.tv.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.animation.slideFromEndEntryMetadata
import sk.styk.martin.apkanalyzer.core.navigation.Navigator
import sk.styk.martin.apkanalyzer.feature.appdetail.api.AppDetailInput
import sk.styk.martin.apkanalyzer.feature.appdetail.api.AppDetailNavKey
import sk.styk.martin.apkanalyzer.feature.browse.api.BrowseNavKey
import sk.styk.martin.apkanalyzer.feature.browse.tv.TvBrowseScreen
import sk.styk.martin.apkanalyzer.feature.browse.tv.apps.TvBrowseAppsScreen
import sk.styk.martin.apkanalyzer.feature.browse.tv.options.TvBrowseOptionsScreen

fun EntryProviderScope<NavKey>.tvBrowseEntries(navigator: Navigator) {
    entry<BrowseNavKey> {
        TvBrowseScreen(
            onOpenDimension = { dimension -> navigator.navigate(TvBrowseOptionsNavKey(dimension)) },
            onOpenOption = { dimension, bucketKey, bucketLabel, subAttribute ->
                navigator.navigate(TvBrowseAppsNavKey(dimension, bucketKey, bucketLabel, subAttribute))
            },
        )
    }

    entry<TvBrowseOptionsNavKey>(metadata = slideFromEndEntryMetadata()) { key ->
        TvBrowseOptionsScreen(
            dimension = key.dimension,
            onOpenOption = { bucketKey, bucketLabel, subAttribute ->
                navigator.navigate(TvBrowseAppsNavKey(key.dimension, bucketKey, bucketLabel, subAttribute))
            },
        )
    }

    entry<TvBrowseAppsNavKey>(metadata = slideFromEndEntryMetadata()) { key ->
        TvBrowseAppsScreen(
            dimension = key.dimension,
            bucketKey = key.bucketKey,
            bucketLabel = key.bucketLabel,
            subAttribute = key.subAttribute,
            onOpenApp = { packageName -> navigator.navigate(AppDetailNavKey(AppDetailInput.InstalledPackage(packageName.value))) },
        )
    }
}
