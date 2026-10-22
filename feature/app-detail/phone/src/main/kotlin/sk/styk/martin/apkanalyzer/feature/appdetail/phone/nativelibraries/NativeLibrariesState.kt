package sk.styk.martin.apkanalyzer.feature.appdetail.phone.nativelibraries

import androidx.compose.runtime.Immutable
import sk.styk.martin.apkanalyzer.core.apps.packaging.NativeLibraryItem

@Immutable
internal sealed interface NativeLibrariesState {
    data object Loading : NativeLibrariesState

    data object Error : NativeLibrariesState

    @Immutable
    data class Loaded(
        val query: String,
        val totalCount: Int,
        val items: List<NativeLibraryItem>,
    ) : NativeLibrariesState {
        val hasResults: Boolean get() = items.isNotEmpty()
    }
}
