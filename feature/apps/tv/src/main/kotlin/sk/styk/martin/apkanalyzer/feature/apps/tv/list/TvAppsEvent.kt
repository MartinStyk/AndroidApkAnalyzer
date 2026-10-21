package sk.styk.martin.apkanalyzer.feature.apps.tv.list

import sk.styk.martin.apkanalyzer.core.common.model.PackageName

internal sealed interface TvAppsEvent {
    data class NavigateToAppDetail(val packageName: PackageName) : TvAppsEvent
    data object OpenUsageAccessSettings : TvAppsEvent
    data object NavigateToSearch : TvAppsEvent
    data object NavigateToFilters : TvAppsEvent
}
