package sk.styk.martin.apkanalyzer.feature.browse.phone

import sk.styk.martin.apkanalyzer.feature.browse.phone.model.BrowseDimension

internal sealed interface BrowseEvent {
    data class NavigateToDimension(val dimension: BrowseDimension) : BrowseEvent
}
