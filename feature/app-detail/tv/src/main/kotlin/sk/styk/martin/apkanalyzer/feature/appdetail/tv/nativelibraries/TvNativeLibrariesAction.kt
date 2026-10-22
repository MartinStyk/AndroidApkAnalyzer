package sk.styk.martin.apkanalyzer.feature.appdetail.tv.nativelibraries

internal sealed interface TvNativeLibrariesAction {
    data object Retry : TvNativeLibrariesAction
}
