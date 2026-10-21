package sk.styk.martin.apkanalyzer.feature.appdetail.tv.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import sk.styk.martin.apkanalyzer.feature.appdetail.api.AppDetailNavKey
import sk.styk.martin.apkanalyzer.feature.appdetail.tv.TvAppDetailScreen

fun EntryProviderScope<NavKey>.tvAppDetailEntries() {
    entry<AppDetailNavKey> { key ->
        TvAppDetailScreen(appDetailInput = key.detailInput)
    }
}
