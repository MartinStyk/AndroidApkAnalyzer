package sk.styk.martin.apkanalyzer.feature.apps.phone.components.apkfilepicker

import android.net.Uri

internal sealed interface ApkFilePickerAction {
    data object OpenPicker : ApkFilePickerAction
    data class ApkSelected(val uri: Uri, val taskId: Int) : ApkFilePickerAction
    data class ApkDetailOpened(val apkFilePath: String) : ApkFilePickerAction
}
