package sk.styk.martin.apkanalyzer.feature.browse.tv.options

import sk.styk.martin.apkanalyzer.core.appindex.browse.BrowseOption
import sk.styk.martin.apkanalyzer.core.appindex.browse.BrowseSubAttribute

internal sealed interface TvBrowseOptionsAction {
    data class ChangeQuery(val query: String) : TvBrowseOptionsAction
    data class SelectSubAttribute(val subAttribute: BrowseSubAttribute) : TvBrowseOptionsAction
    data class FocusOption(val option: BrowseOption) : TvBrowseOptionsAction
    data class SelectOption(val option: BrowseOption) : TvBrowseOptionsAction
}
