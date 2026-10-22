package sk.styk.martin.apkanalyzer.feature.apps.tv.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import sk.styk.martin.apkanalyzer.core.common.model.PackageName
import sk.styk.martin.apkanalyzer.core.designsystem.foundation.animation.bottomEntryMetadata
import sk.styk.martin.apkanalyzer.core.navigation.Navigator
import sk.styk.martin.apkanalyzer.feature.appdetail.api.AppDetailInput
import sk.styk.martin.apkanalyzer.feature.appdetail.api.AppDetailNavKey
import sk.styk.martin.apkanalyzer.feature.apps.api.AppsNavKey
import sk.styk.martin.apkanalyzer.feature.apps.tv.filter.TvAppFilterScreen
import sk.styk.martin.apkanalyzer.feature.apps.tv.list.TvAppsScreen
import sk.styk.martin.apkanalyzer.feature.apps.tv.search.TvAppSearchScreen

fun EntryProviderScope<NavKey>.tvAppEntries(navigator: Navigator) {
    val openAppDetail: (PackageName) -> Unit = { packageName ->
        navigator.navigate(AppDetailNavKey(AppDetailInput.InstalledPackage(packageName.value)))
    }

    entry<AppsNavKey> {
        TvAppsScreen(
            onOpenAppDetail = openAppDetail,
            onOpenSearch = { navigator.navigate(TvAppSearchNavKey) },
            onOpenFilters = { navigator.navigate(TvAppFilterNavKey) },
        )
    }

    entry<TvAppFilterNavKey>(metadata = bottomEntryMetadata()) {
        TvAppFilterScreen()
    }

    entry<TvAppSearchNavKey> {
        TvAppSearchScreen(onOpenAppDetail = openAppDetail)
    }
}
