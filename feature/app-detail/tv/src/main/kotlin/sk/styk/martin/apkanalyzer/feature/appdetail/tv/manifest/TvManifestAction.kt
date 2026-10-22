package sk.styk.martin.apkanalyzer.feature.appdetail.tv.manifest

internal sealed interface TvManifestAction {
    data object Retry : TvManifestAction
}
