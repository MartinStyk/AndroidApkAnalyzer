package sk.styk.martin.apkanalyzer.feature.browse.tv.options

import androidx.compose.runtime.Immutable
import sk.styk.martin.apkanalyzer.core.appindex.browse.BrowseOption
import sk.styk.martin.apkanalyzer.core.appindex.browse.BrowseSubAttribute

@Immutable
internal sealed interface TvBrowseOptionsState {

    data object Loading : TvBrowseOptionsState

    @Immutable
    data class Loaded(
        val query: String,
        val subAttribute: BrowseSubAttribute?,
        val subAttributes: List<BrowseSubAttribute>,
        val totalOptions: Int,
        val options: List<BrowseOption>,
    ) : TvBrowseOptionsState
}
