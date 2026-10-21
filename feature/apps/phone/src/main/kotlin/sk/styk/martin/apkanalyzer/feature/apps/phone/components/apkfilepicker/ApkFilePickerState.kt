package sk.styk.martin.apkanalyzer.feature.apps.phone.components.apkfilepicker

internal sealed interface ApkFilePickerState {
    data object Ready : ApkFilePickerState
    data object Copying : ApkFilePickerState
}
