package sk.styk.martin.apkanalyzer.feature.appdetail.tv.manifest

import androidx.compose.runtime.Immutable

@Immutable
internal sealed interface TvManifestState {
    data object Loading : TvManifestState

    data object Error : TvManifestState

    @Immutable
    data class Loaded(val lines: List<String>, val additionalInstalledSplits: Int) : TvManifestState
}
