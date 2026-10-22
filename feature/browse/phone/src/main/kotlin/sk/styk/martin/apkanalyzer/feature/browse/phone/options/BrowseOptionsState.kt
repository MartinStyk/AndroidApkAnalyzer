package sk.styk.martin.apkanalyzer.feature.browse.phone.options

import androidx.compose.runtime.Immutable
import sk.styk.martin.apkanalyzer.core.appindex.browse.BrowseOption
import sk.styk.martin.apkanalyzer.core.appindex.browse.BrowseSubAttribute

@Immutable
internal sealed interface BrowseOptionsState {

    data object Loading : BrowseOptionsState

    @Immutable
    data class Loaded(
        val query: String,
        val subAttribute: BrowseSubAttribute?,
        val totalOptions: Int,
        val options: List<BrowseOption>,
    ) : BrowseOptionsState
}
