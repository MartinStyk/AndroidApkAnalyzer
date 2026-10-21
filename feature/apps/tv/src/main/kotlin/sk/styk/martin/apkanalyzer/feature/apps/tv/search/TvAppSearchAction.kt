package sk.styk.martin.apkanalyzer.feature.apps.tv.search

import sk.styk.martin.apkanalyzer.core.common.model.PackageName

internal sealed interface TvAppSearchAction {
    data class QueryChanged(val query: String) : TvAppSearchAction
    data class ResultClicked(val packageName: PackageName) : TvAppSearchAction
    data class HistoryItemClicked(val packageName: PackageName) : TvAppSearchAction
    data object ClearHistory : TvAppSearchAction
}
