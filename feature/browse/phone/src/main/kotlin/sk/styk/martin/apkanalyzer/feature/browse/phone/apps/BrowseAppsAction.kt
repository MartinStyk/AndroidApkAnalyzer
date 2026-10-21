package sk.styk.martin.apkanalyzer.feature.browse.phone.apps

import sk.styk.martin.apkanalyzer.core.common.model.PackageName

internal sealed interface BrowseAppsAction {
    data class ChangeQuery(val query: String) : BrowseAppsAction
    data class AppClicked(val packageName: PackageName) : BrowseAppsAction
}
