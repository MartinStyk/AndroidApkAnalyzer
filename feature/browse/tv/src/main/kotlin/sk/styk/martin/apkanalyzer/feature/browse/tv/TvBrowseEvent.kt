package sk.styk.martin.apkanalyzer.feature.browse.tv

import sk.styk.martin.apkanalyzer.core.appindex.browse.BrowseDimension

internal sealed interface TvBrowseEvent {
    data class NavigateToDimension(val dimension: BrowseDimension) : TvBrowseEvent
}
