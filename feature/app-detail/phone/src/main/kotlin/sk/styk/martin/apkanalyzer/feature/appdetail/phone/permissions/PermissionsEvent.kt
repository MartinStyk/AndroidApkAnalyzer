package sk.styk.martin.apkanalyzer.feature.appdetail.phone.permissions

internal sealed interface PermissionsEvent {
    data object ShowCopiedFeedback : PermissionsEvent
}
