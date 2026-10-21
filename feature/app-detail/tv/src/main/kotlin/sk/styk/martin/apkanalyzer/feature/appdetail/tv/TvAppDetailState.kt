package sk.styk.martin.apkanalyzer.feature.appdetail.tv

import androidx.compose.runtime.Immutable
import sk.styk.martin.apkanalyzer.core.common.model.AppReference

@Immutable
internal sealed interface TvAppDetailState {
    data object Loading : TvAppDetailState

    data object Error : TvAppDetailState

    @Immutable
    data class Loaded(
        val appReference: AppReference,
        val appName: String,
        val packageName: String,
        val versionName: String?,
    ) : TvAppDetailState
}
