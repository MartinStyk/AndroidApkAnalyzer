package sk.styk.martin.apkanalyzer.feature.browse.phone

import androidx.compose.runtime.Immutable
import sk.styk.martin.apkanalyzer.feature.browse.phone.model.BrowseDimension

@Immutable
internal sealed interface BrowseState {

    data object Loading : BrowseState

    @Immutable
    data class Loaded(val totalApps: Int, val dimensions: List<DimensionSummary>) : BrowseState
}

@Immutable
internal data class DimensionSummary(
    val dimension: BrowseDimension,
    val optionCount: Int,
    val topLabels: List<String>,
)
