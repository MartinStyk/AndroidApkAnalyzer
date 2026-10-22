package sk.styk.martin.apkanalyzer.feature.browse.tv.apps

import sk.styk.martin.apkanalyzer.core.common.model.PackageName

internal sealed interface TvBrowseAppsEvent {
    data class NavigateToAppDetail(val packageName: PackageName) : TvBrowseAppsEvent
}
