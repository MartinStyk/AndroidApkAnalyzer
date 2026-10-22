package sk.styk.martin.apkanalyzer.feature.browse.phone.options

import sk.styk.martin.apkanalyzer.core.appindex.browse.BrowseOption
import sk.styk.martin.apkanalyzer.core.appindex.browse.BrowseSubAttribute

internal sealed interface BrowseOptionsAction {
    data class ChangeQuery(val query: String) : BrowseOptionsAction
    data class SelectSubAttribute(val subAttribute: BrowseSubAttribute) : BrowseOptionsAction
    data class SelectOption(val option: BrowseOption) : BrowseOptionsAction
}
