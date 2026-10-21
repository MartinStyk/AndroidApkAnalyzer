package sk.styk.martin.apkanalyzer.feature.appdetail.phone.manifest

internal sealed interface ManifestAction {
    data object Retry : ManifestAction
    data class ChangeQuery(val query: String) : ManifestAction
}
