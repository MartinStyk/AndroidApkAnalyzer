package sk.styk.martin.apkanalyzer.feature.apps.tv.search

import sk.styk.martin.apkanalyzer.core.common.model.PackageName

internal sealed interface TvAppSearchEvent {
    data class NavigateToAppDetail(val packageName: PackageName) : TvAppSearchEvent
}
