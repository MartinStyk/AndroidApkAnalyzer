package sk.styk.martin.apkanalyzer.feature.apps.phone.filter.sdkversion

sealed interface SdkVersionFilterEvent {
    data object NavigateBack : SdkVersionFilterEvent
}
