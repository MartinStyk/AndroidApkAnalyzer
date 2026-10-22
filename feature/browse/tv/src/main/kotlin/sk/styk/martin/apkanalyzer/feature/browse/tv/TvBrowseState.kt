package sk.styk.martin.apkanalyzer.feature.browse.tv

import androidx.compose.runtime.Immutable
import sk.styk.martin.apkanalyzer.core.appindex.browse.BrowseDimensionSummary

@Immutable
internal sealed interface TvBrowseState {

    data object Loading : TvBrowseState

    @Immutable
    data class Loaded(val totalApps: Int, val dimensions: List<BrowseDimensionSummary>) : TvBrowseState
}
