package sk.styk.martin.apkanalyzer.feature.apps.phone.filter.permission

sealed interface PermissionFilterEvent {
    data object NavigateBack : PermissionFilterEvent
}
