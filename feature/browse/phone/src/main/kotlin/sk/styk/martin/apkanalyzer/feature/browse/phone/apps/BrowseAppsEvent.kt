package sk.styk.martin.apkanalyzer.feature.browse.phone.apps

import sk.styk.martin.apkanalyzer.core.common.model.PackageName

internal sealed interface BrowseAppsEvent {
    data class NavigateToAppDetail(val packageName: PackageName) : BrowseAppsEvent
}
