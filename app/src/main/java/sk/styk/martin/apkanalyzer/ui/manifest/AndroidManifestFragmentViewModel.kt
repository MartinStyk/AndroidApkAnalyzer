package sk.styk.martin.apkanalyzer.ui.manifest

import android.app.Activity
import android.net.Uri
import android.view.MenuItem
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.core.appanalysis.AndroidManifestManager
import sk.styk.martin.apkanalyzer.manager.file.FileManager
import sk.styk.martin.apkanalyzer.manager.notification.NotificationManager
import sk.styk.martin.apkanalyzer.manager.permission.PermissionManager
import sk.styk.martin.apkanalyzer.manager.permission.withNotificationPermission
import sk.styk.martin.apkanalyzer.manager.permission.withStoragePermission
import sk.styk.martin.apkanalyzer.util.OutputFilePickerRequest
import sk.styk.martin.apkanalyzer.util.TAG_EXPORTS
import sk.styk.martin.apkanalyzer.util.TextInfo
import sk.styk.martin.apkanalyzer.util.components.SnackBarComponent
import sk.styk.martin.apkanalyzer.core.common.coroutines.DispatcherProvider
import sk.styk.martin.apkanalyzer.util.live.SingleLiveEvent
import timber.log.Timber
import java.io.IOException

private const val LOADING_STATE = 0
private const val ERROR_STATE = 1
private const val DATA_STATE = 2

class AndroidManifestFragmentViewModel @AssistedInject constructor(
    @Assisted private val manifestRequest: ManifestRequest,
    private val permissionManager: PermissionManager,
    private val androidManifestManager: AndroidManifestManager,
    private val dispatcherProvider: DispatcherProvider,
    private val fileManager: FileManager,
    private val notificationManager: NotificationManager,
) : ViewModel(), Toolbar.OnMenuItemClickListener {

    private val viewStateLiveData = MutableLiveData(LOADING_STATE)
    val viewState: LiveData<Int> = viewStateLiveData

    val appName: LiveData<String> = MutableLiveData(manifestRequest.appName)

    private val closeEvent = SingleLiveEvent<Unit>()
    val close: LiveData<Unit> = closeEvent

    private val openExportFilePickerEvent = SingleLiveEvent<OutputFilePickerRequest>()
    val openExportFilePicker: LiveData<OutputFilePickerRequest> = openExportFilePickerEvent

    private val showSnackEvent = SingleLiveEvent<SnackBarComponent>()
    val showSnack: LiveData<SnackBarComponent> = showSnackEvent

    private val showManifestFileEvent = SingleLiveEvent<Uri>()
    val showManifestFile: LiveData<Uri> = showManifestFileEvent

    private val manifestLiveData = MutableLiveData<String>()
    val manifest: LiveData<String> = manifestLiveData

    val exportFilePickerResult = ActivityResultCallback<ActivityResult> {
        if (it.resultCode == Activity.RESULT_OK) {
            val uri = it.data?.data
            val manifest = manifest.value
            if (uri != null && !manifest.isNullOrBlank()) {
                internalExport(uri, manifest)
            } else {
                cannotSaveManifest()
            }
        }
    }

    init {
        viewModelScope.launch(dispatcherProvider.main()) {
            val manifest = try {
                androidManifestManager.loadAndroidManifest(manifestRequest.packageName, manifestRequest.apkPath)
            } catch (e: Exception) {
                Timber.tag(TAG_EXPORTS).e(e, "Error loading manifest for $manifestRequest.")
                ""
            }
            manifestLiveData.postValue(manifest)
            viewStateLiveData.postValue(if (manifest.isBlank()) ERROR_STATE else DATA_STATE)
        }
    }

    fun onNavigationClick() = closeEvent.call()

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_save) {
            onExportManifestClick()
            true
        } else {
            false
        }
    }

    private fun onExportManifestClick() {
        permissionManager.withStoragePermission(withoutPermission = {
            showSnackEvent.value = SnackBarComponent(TextInfo.from(R.string.permission_not_granted), Snackbar.LENGTH_LONG)
        }) {
            exportAppFileSelection()
        }
    }

    private fun exportAppFileSelection() {
        val value = manifest.value
        if (value.isNullOrBlank()) {
            showSnackEvent.value = SnackBarComponent(TextInfo.from(R.string.save_manifest_fail))
        } else {
            openExportFilePickerEvent.value = OutputFilePickerRequest("${manifestRequest.packageName}_${manifestRequest.versionName}_AndroidManifest.xml", "text/xml")
        }
    }

    private fun internalExport(target: Uri, manifest: String) = viewModelScope.launch {
        try {
            withContext(dispatcherProvider.io()) {
                fileManager.writeString(manifest, target)
            }
            showSnackEvent.value = SnackBarComponent(
                TextInfo.from(R.string.manifest_saved, manifestRequest.appName),
                duration = Snackbar.LENGTH_LONG,
                action = TextInfo.from(R.string.action_show),
            ) {
                showManifestFileEvent.value = target
            }
            permissionManager.withNotificationPermission {
                notificationManager.showManifestSavedNotification(manifestRequest.appName, target)
            }
        } catch (e: IOException) {
            Timber.tag(TAG_EXPORTS).e(e, "Error saving manifest for $manifestRequest. Target was $target")
            cannotSaveManifest()
        }
    }

    private fun cannotSaveManifest() {
        showSnackEvent.postValue(SnackBarComponent(TextInfo.from(R.string.can_not_save_manifest)))
    }

    @AssistedFactory
    interface Factory {
        fun create(manifestRequest: ManifestRequest): AndroidManifestFragmentViewModel
    }
}
