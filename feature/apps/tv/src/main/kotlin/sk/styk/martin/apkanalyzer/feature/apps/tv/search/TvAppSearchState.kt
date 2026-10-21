package sk.styk.martin.apkanalyzer.feature.apps.tv.search

import androidx.compose.runtime.Immutable
import sk.styk.martin.apkanalyzer.feature.apps.tv.components.TvAppItem

@Immutable
internal data class TvAppSearchState(
    val query: String = "",
    val results: List<TvAppItem> = listOf(),
    val history: List<TvSearchHistoryItem> = listOf(),
)

@Immutable
internal data class TvSearchHistoryItem(val app: TvAppItem, val query: String)
