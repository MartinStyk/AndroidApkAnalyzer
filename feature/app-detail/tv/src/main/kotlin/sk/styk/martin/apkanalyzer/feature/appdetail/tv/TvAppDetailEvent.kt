package sk.styk.martin.apkanalyzer.feature.appdetail.tv

import sk.styk.martin.apkanalyzer.core.common.model.PackageName

internal sealed interface TvAppDetailEvent {
    data class OpenAppInfo(val packageName: PackageName) : TvAppDetailEvent
    data class OpenPlayStore(val packageName: PackageName) : TvAppDetailEvent
    data object NavigateToGeneralInfo : TvAppDetailEvent
    data class NavigateToPermissions(val permissionName: String?) : TvAppDetailEvent
    data object NavigateToComponents : TvAppDetailEvent
    data object NavigateToCertificates : TvAppDetailEvent
    data object NavigateToRequirements : TvAppDetailEvent
    data object NavigateToManifest : TvAppDetailEvent
}
