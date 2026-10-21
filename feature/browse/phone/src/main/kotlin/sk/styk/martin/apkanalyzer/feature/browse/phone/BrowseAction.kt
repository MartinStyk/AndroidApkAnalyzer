package sk.styk.martin.apkanalyzer.feature.browse.phone

import sk.styk.martin.apkanalyzer.feature.browse.phone.model.BrowseDimension

internal sealed interface BrowseAction {
    data class SelectDimension(val dimension: BrowseDimension) : BrowseAction
}
