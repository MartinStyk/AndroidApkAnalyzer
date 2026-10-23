package sk.styk.martin.apkanalyzer.feature.browse.tv

import sk.styk.martin.apkanalyzer.core.appindex.browse.BrowseDimension
import sk.styk.martin.apkanalyzer.core.appindex.browse.BrowseOption
import sk.styk.martin.apkanalyzer.core.appindex.browse.BrowseSubAttribute

internal sealed interface TvBrowseAction {
    data class SelectDimension(val dimension: BrowseDimension) : TvBrowseAction
    data class SelectOption(
        val dimension: BrowseDimension,
        val option: BrowseOption,
        val subAttribute: BrowseSubAttribute?,
    ) : TvBrowseAction
}
