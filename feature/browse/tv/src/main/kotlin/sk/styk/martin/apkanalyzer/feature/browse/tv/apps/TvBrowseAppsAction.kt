package sk.styk.martin.apkanalyzer.feature.browse.tv.apps

import sk.styk.martin.apkanalyzer.core.common.model.PackageName

internal sealed interface TvBrowseAppsAction {
    data class ChangeQuery(val query: String) : TvBrowseAppsAction
    data class SelectApp(val packageName: PackageName) : TvBrowseAppsAction
}
