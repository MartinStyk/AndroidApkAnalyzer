package sk.styk.martin.apkanalyzer.ui.manifest

import android.Manifest
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.launch
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.business.analysis.logic.AndroidManifestManager
import sk.styk.martin.apkanalyzer.manager.file.FileManager
import sk.styk.martin.apkanalyzer.manager.notification.NotificationManager
import sk.styk.martin.apkanalyzer.manager.permission.PermissionManager
import sk.styk.martin.apkanalyzer.ui.applist.LOADING_STATE
import sk.styk.martin.apkanalyzer.util.TextInfo
import sk.styk.martin.apkanalyzer.util.components.SnackBarComponent
import sk.styk.martin.apkanalyzer.util.coroutines.DispatcherProvider
import sk.styk.martin.apkanalyzer.util.file.FileUtils
import sk.styk.martin.apkanalyzer.util.live.SingleLiveEvent
import java.io.File
import java.io.IOException

private const val LOADING_STATE = 0
private const val ERROR_STATE = 1
private const val DATA_STATE = 2

private const val MANIFEST_SUBDIR = "Manifest"

class AndroidManifestFragmentViewModel @AssistedInject constructor(
        @Assisted private val manifestRequest: ManifestRequest,
        private val permissionManager: PermissionManager,
        private val androidManifestManager: AndroidManifestManager,
        private val dispatcherProvider: DispatcherProvider,
        private val fileManager: FileManager,
        private val notificationManager: NotificationManager,
) : ViewModel(), Toolbar.OnMenuItemClickListener {

    protected val viewStateLiveData = MutableLiveData(LOADING_STATE)
    val viewState: LiveData<Int> = viewStateLiveData

    val appName: LiveData<String> = MutableLiveData(manifestRequest.appName)

    private val closeEvent = SingleLiveEvent<Unit>()
    val close: LiveData<Unit> = closeEvent

    private val showSnackEvent = SingleLiveEvent<SnackBarComponent>()
    val showSnack: LiveData<SnackBarComponent> = showSnackEvent

    private val showManifestFileEvent = SingleLiveEvent<File>()
    val showManifestFile: LiveData<File> = showManifestFileEvent

    private val manifestLiveData = MutableLiveData<String>()
    val manifest: LiveData<String> = manifestLiveData

    init {
        viewModelScope.launch(dispatcherProvider.main()) {
            val manifest = try {
                androidManifestManager.loadAndroidManifest(manifestRequest.packageName, manifestRequest.apkPath)
            } catch (e: Exception) {
                ""
            }
            Log.e("XXX", "manifest $manifest")
            manifestLiveData.postValue(manifest)
            viewStateLiveData.postValue(if (manifest.isBlank()) ERROR_STATE else DATA_STATE)
        }
    }

    fun onNavigationClick() = closeEvent.call()

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_save) {
            exportManifest()
            true
        } else {
            false
        }
    }

    private fun exportManifest() {
        fun export() {
            val value = manifest.value
            if (value.isNullOrBlank()) {
                showSnackEvent.value = SnackBarComponent(TextInfo.from(R.string.save_manifest_fail))
            } else {
                try {
                    val target = File(fileManager.externalDirectory, "$MANIFEST_SUBDIR/${manifestRequest.packageName}_${manifestRequest.versionName}_AndroidManifest.xml")
                    FileUtils.writeString(value, target)
                    showSnackEvent.value = SnackBarComponent(TextInfo.from(R.string.manifest_saved),
                            duration = Snackbar.LENGTH_LONG,
                            action = TextInfo.from(R.string.action_show)) {
                        showManifestFileEvent.value = target
                    }
                    notificationManager.showManifestSavedNotification(manifestRequest.appName, target)
                } catch (e: IOException) {
                    e.printStackTrace()
                    showSnackEvent.value = SnackBarComponent(TextInfo.from(R.string.can_not_save_manifest))
                }
            }
        }

        if (permissionManager.hasPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            export()
        } else {
            permissionManager.requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, object : PermissionManager.PermissionCallback {
                override fun onPermissionDenied(permission: String) {
                    showSnackEvent.value = SnackBarComponent(TextInfo.from(R.string.permission_not_granted), Snackbar.LENGTH_LONG)
                }

                override fun onPermissionGranted(permission: String) {
                    export()
                }
            })
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(manifestRequest: ManifestRequest): AndroidManifestFragmentViewModel
    }

}
