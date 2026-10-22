package sk.styk.martin.apkanalyzer.feature.browse.tv

import sk.styk.martin.apkanalyzer.core.appindex.browse.BrowseDimension

internal sealed interface TvBrowseAction {
    data class SelectDimension(val dimension: BrowseDimension) : TvBrowseAction
}
