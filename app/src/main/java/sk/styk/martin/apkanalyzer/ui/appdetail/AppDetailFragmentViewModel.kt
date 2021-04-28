package sk.styk.martin.apkanalyzer.ui.appdetail

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.lifecycle.*
import androidx.palette.graphics.Target
import androidx.palette.graphics.get
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.manager.analytics.AnalyticsTracker
import sk.styk.martin.apkanalyzer.manager.appanalysis.AppDetailDataManager
import sk.styk.martin.apkanalyzer.manager.file.ApkSaveManager
import sk.styk.martin.apkanalyzer.manager.file.DrawableSaveManager
import sk.styk.martin.apkanalyzer.manager.file.FileManager
import sk.styk.martin.apkanalyzer.manager.notification.NotificationManager
import sk.styk.martin.apkanalyzer.manager.permission.PermissionManager
import sk.styk.martin.apkanalyzer.manager.permission.hasScopedStorage
import sk.styk.martin.apkanalyzer.manager.resources.ActivityColorThemeManager
import sk.styk.martin.apkanalyzer.manager.resources.ResourcesManager
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.ui.manifest.ManifestRequest
import sk.styk.martin.apkanalyzer.util.*
import sk.styk.martin.apkanalyzer.util.components.SnackBarComponent
import sk.styk.martin.apkanalyzer.util.coroutines.DispatcherProvider
import sk.styk.martin.apkanalyzer.util.live.SingleLiveEvent
import timber.log.Timber
import java.io.File
import kotlin.math.abs

internal const val LOADING_STATE = 0
internal const val ERROR_STATE = 1
internal const val DATA_STATE = 2

private const val ANALYZED_APK_NAME = "analyzed.apk"

@OptIn(ExperimentalCoroutinesApi::class)
class AppDetailFragmentViewModel @AssistedInject constructor(
        @Assisted val appDetailRequest: AppDetailRequest,
        private val dispatcherProvider: DispatcherProvider,
        private val appDetailDataManager: AppDetailDataManager,
        private val resourcesManager: ResourcesManager,
        private val permissionManager: PermissionManager,
        private val appActionsAdapter: AppActionsSpeedMenuAdapter,
        private val drawableSaveManager: DrawableSaveManager,
        private val notificationManager: NotificationManager,
        private val apkSaveManager: ApkSaveManager,
        private val fileManager: FileManager,
        private val packageManager: PackageManager,
        private val activityColorThemeManager: ActivityColorThemeManager,
        private val analyticsTracker: AnalyticsTracker,
) : ViewModel(), AppBarLayout.OnOffsetChangedListener, DefaultLifecycleObserver {

    private val viewStateLiveData = MutableLiveData(LOADING_STATE)
    val viewState: LiveData<Int> = viewStateLiveData

    private val appDetailsLiveData = MutableLiveData<AppDetailData>()
    val appDetails: LiveData<AppDetailData> = appDetailsLiveData

    private val actionButtonVisibilityLiveData = MutableLiveData(false)
    val actionButtonVisibility: LiveData<Boolean> = actionButtonVisibilityLiveData

    private val actionButtonAdapterLiveData = MutableLiveData<AppActionsSpeedMenuAdapter>()
    val actionButtonAdapter: LiveData<AppActionsSpeedMenuAdapter> = actionButtonAdapterLiveData

    private val showSnackEvent = SingleLiveEvent<SnackBarComponent>()
    val showSnack: LiveData<SnackBarComponent> = showSnackEvent

    private val closeEvent = SingleLiveEvent<Unit>()
    val close: LiveData<Unit> = closeEvent

    private val installAppEvent = SingleLiveEvent<String>()
    val installApp: LiveData<String> = installAppEvent

    private val openSettingsInstallPermissionEvent = SingleLiveEvent<Unit>()
    val openSettingsInstallPermission: LiveData<Unit> = openSettingsInstallPermissionEvent

    private val openImageEvent = SingleLiveEvent<Uri>()
    val openImage: LiveData<Uri> = openImageEvent

    private val openExportFilePickerEvent = SingleLiveEvent<OutputFilePickerRequest>()
    val openExportFilePicker: LiveData<OutputFilePickerRequest> = openExportFilePickerEvent

    private val showManifestEvent = SingleLiveEvent<ManifestRequest>()
    val showManifest: LiveData<ManifestRequest> = showManifestEvent

    private val openGooglePlayEvent = SingleLiveEvent<String>()
    val openGooglePlay: LiveData<String> = openGooglePlayEvent

    private val openSystemInfoEvent = SingleLiveEvent<String>()
    val openSystemInfo: LiveData<String> = openSystemInfoEvent

    val toolbarTitle: LiveData<TextInfo> = viewStateLiveData.map {
        val details = appDetails.value
        when {
            it == LOADING_STATE -> TextInfo.from(R.string.loading)
            details != null -> TextInfo.from(details.generalData.applicationName)
            else -> TextInfo.from(R.string.loading_failed)
        }
    }

    val toolbarSubtitle: LiveData<TextInfo> = appDetails.map { TextInfo.from(it.generalData.packageName) }
    val toolbarSubtitleVisibility: LiveData<Boolean> = viewStateLiveData.map { it == DATA_STATE }

    private val accentColorLiveData = MutableLiveData(ColorInfo.SECONDARY)
    val accentColor: LiveData<ColorInfo> = accentColorLiveData

    val toolbarIcon: LiveData<Drawable> = appDetails.map { it.generalData.icon!! }

    val installPermissionResult = ActivityResultCallback<ActivityResult> {
        val apkPath = appDetailsLiveData.value?.generalData?.apkDirectory
                ?: return@ActivityResultCallback
        if (it?.resultCode == Activity.RESULT_OK && !apkPath.isBlank() && (Build.VERSION.SDK_INT < Build.VERSION_CODES.O || packageManager.canRequestPackageInstalls())) {
            installAppEvent.value = apkPath
        }
    }

    val exportFilePickerResult = ActivityResultCallback<ActivityResult> {
        if (it.resultCode == Activity.RESULT_OK) {
            val uri = it.data?.data
            if (uri != null) {
                saveApk(uri)
            } else {
                showSnackEvent.value = SnackBarComponent(TextInfo.from(R.string.app_export_failed))
            }
        }
    }

    init {
        loadDetail()
        observeApkActions()
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        updateActionButtonAdapter()
    }

    private fun loadDetail() = viewModelScope.launch {
        try {
            val detail = withContext(dispatcherProvider.default()) {
                when (appDetailRequest) {
                    is AppDetailRequest.InstalledPackage -> appDetailDataManager.loadForInstalledPackage(appDetailRequest.packageName)
                    is AppDetailRequest.ExternalPackage -> {
                        val tempFile = fileManager.createTempFileFromUri(appDetailRequest.packageUri, ANALYZED_APK_NAME)
                        appDetailDataManager.loadForExternalPackage(tempFile)
                    }
                }
            }
            setupToolbar(detail)
            appDetailsLiveData.value = detail
            viewStateLiveData.value = DATA_STATE
            actionButtonVisibilityLiveData.value = true
        } catch (e: Exception) {
            viewStateLiveData.value = ERROR_STATE
            actionButtonVisibilityLiveData.value = false
            Timber.tag(TAG_APP_DETAIL).w(e, "Loading detail for $appDetailRequest failed")
        }
    }

    private suspend fun setupToolbar(detail: AppDetailData) {
        val palette = resourcesManager.generatePalette(detail.generalData.icon!!)

        val range = if (activityColorThemeManager.isNightMode()) 0.1..0.35 else 0.09..0.45

        val accentColor = listOf(Target.DARK_VIBRANT, Target.DARK_MUTED, Target.VIBRANT, Target.MUTED, Target.LIGHT_VIBRANT, Target.LIGHT_MUTED)
                .mapNotNull { palette[it]?.rgb }
                .firstOrNull { resourcesManager.luminance(it) in range }
                ?.let { ColorInfo.fromColorInt(it) }

        accentColorLiveData.postValue(accentColor ?: ColorInfo.SECONDARY)
    }

    override fun onOffsetChanged(bar: AppBarLayout, verticalOffset: Int) {
        actionButtonVisibilityLiveData.value = (abs(verticalOffset) - bar.totalScrollRange != 0) && viewState.value == DATA_STATE
    }

    private fun updateActionButtonAdapter() {
        val displayHeight = resourcesManager.getDisplayHeight()
        appActionsAdapter.menuItems = when (appDetailRequest) {
            is AppDetailRequest.ExternalPackage ->
                if (displayHeight < 420) {
                    listOf(AppActionsSpeedMenuAdapter.AppActions.SAVE_ICON, AppActionsSpeedMenuAdapter.AppActions.SHOW_MANIFEST, AppActionsSpeedMenuAdapter.AppActions.INSTALL)
                } else {
                    listOf(AppActionsSpeedMenuAdapter.AppActions.OPEN_PLAY, AppActionsSpeedMenuAdapter.AppActions.SAVE_ICON, AppActionsSpeedMenuAdapter.AppActions.SHOW_MANIFEST, AppActionsSpeedMenuAdapter.AppActions.INSTALL)
                }
            is AppDetailRequest.InstalledPackage ->
                if (displayHeight < 420) {
                    listOf(AppActionsSpeedMenuAdapter.AppActions.SAVE_ICON, AppActionsSpeedMenuAdapter.AppActions.EXPORT_APK, AppActionsSpeedMenuAdapter.AppActions.SHOW_MANIFEST)
                } else {
                    listOf(AppActionsSpeedMenuAdapter.AppActions.OPEN_PLAY, AppActionsSpeedMenuAdapter.AppActions.BUILD_INFO, AppActionsSpeedMenuAdapter.AppActions.SAVE_ICON, AppActionsSpeedMenuAdapter.AppActions.EXPORT_APK, AppActionsSpeedMenuAdapter.AppActions.SHOW_MANIFEST)
                }
        }
        actionButtonAdapterLiveData.value = appActionsAdapter
    }

    private fun observeApkActions() {
        viewModelScope.launch {
            appActionsAdapter.installApp.collect {
                analyticsTracker.trackAppActionAction(AnalyticsTracker.AppAction.INSTALL)
                appDetails.value?.let {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O || packageManager.canRequestPackageInstalls()) {
                        installAppEvent.value = it.generalData.apkDirectory
                    } else {
                        openSettingsInstallPermissionEvent.call()
                    }
                }
            }
        }
        viewModelScope.launch {
            appActionsAdapter.exportApp.collect {
                analyticsTracker.trackAppActionAction(AnalyticsTracker.AppAction.EXPORT_APK)
                appDetails.value?.let { data ->
                    if (hasScopedStorage() || permissionManager.hasPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        exportAppFileSelection()
                    } else {
                        permissionManager.requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, object : PermissionManager.PermissionCallback {
                            override fun onPermissionDenied(permission: String) {
                                showSnackEvent.value = SnackBarComponent(TextInfo.from(R.string.permission_not_granted), Snackbar.LENGTH_LONG)
                            }

                            override fun onPermissionGranted(permission: String) {
                                exportAppFileSelection()
                            }
                        })
                    }
                }
            }
        }
        viewModelScope.launch {
            appActionsAdapter.saveIcon.collect {
                analyticsTracker.trackAppActionAction(AnalyticsTracker.AppAction.SAVE_ICON)
                appDetails.value?.let { data ->
                    if (hasScopedStorage() || permissionManager.hasPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        saveImage()
                    } else {
                        permissionManager.requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, object : PermissionManager.PermissionCallback {
                            override fun onPermissionDenied(permission: String) {
                                showSnackEvent.value = SnackBarComponent(TextInfo.from(R.string.permission_not_granted), Snackbar.LENGTH_LONG)
                            }

                            override fun onPermissionGranted(permission: String) {
                                saveImage()
                            }
                        })
                    }
                }
            }
        }
        viewModelScope.launch {
            analyticsTracker.trackAppActionAction(AnalyticsTracker.AppAction.SHOW_MANIFEST)
            appActionsAdapter.showManifest.collect {
                appDetails.value?.generalData?.let {
                    showManifestEvent.value = ManifestRequest(
                            appName = it.applicationName,
                            packageName = it.packageName,
                            apkPath = it.apkDirectory,
                            versionName = it.versionName,
                            versionCode = it.versionCode)
                }
            }
        }
        viewModelScope.launch {
            analyticsTracker.trackAppActionAction(AnalyticsTracker.AppAction.OPEN_GOOGLE_PLAY)
            appActionsAdapter.openGooglePlay.collect {
                appDetails.value?.let {
                    openGooglePlayEvent.value = it.generalData.packageName
                }
            }
        }
        viewModelScope.launch {
            analyticsTracker.trackAppActionAction(AnalyticsTracker.AppAction.OPEN_SYSTEM_ABOUT)
            appActionsAdapter.openSystemInfo.collect {
                appDetails.value?.let {
                    openSystemInfoEvent.value = it.generalData.packageName
                }
            }
        }
    }

    fun onNavigationClick() {
        closeEvent.call()
    }

    private fun saveImage() {
        val data = appDetails.value ?: return
        val icon = data.generalData.icon
        if (icon != null) {
            viewModelScope.launch {
                val target = "${data.generalData.packageName}_${data.generalData.versionName}_${data.generalData.versionCode}_icon.png"
                try {
                    val exportedFileUri = drawableSaveManager.saveDrawable(icon, target, "image/png")
                    showSnackEvent.value = SnackBarComponent(TextInfo.from(R.string.icon_saved),
                            action = TextInfo.from(R.string.action_show),
                            callback = {
                                openImageEvent.value = exportedFileUri
                            })
                    notificationManager.showImageExportedNotification(data.generalData.applicationName, exportedFileUri)
                } catch (e: Exception) {
                    Timber.tag(TAG_EXPORTS).e(e, "Saving icon failed. Data ${data.generalData}")
                    showSnackEvent.value = SnackBarComponent(TextInfo.from(R.string.icon_export_failed))
                }
            }
        }
    }

    private fun exportAppFileSelection() {
        val data = appDetails.value ?: return
        val source = File(data.generalData.apkDirectory)
        if (!source.exists()) {
            showSnackEvent.value = SnackBarComponent(TextInfo.from(R.string.app_export_failed))
        }
        openExportFilePickerEvent.value = OutputFilePickerRequest("${data.generalData.packageName}_${data.generalData.versionName}.apk", "application/vnd.android.package-archive")
    }

    private fun saveApk(targetUri: Uri) {
        val data = appDetails.value ?: return
        viewModelScope.launch {
            showSnackEvent.value = SnackBarComponent(TextInfo.from(R.string.saving_app, data.generalData.applicationName))
            apkSaveManager.saveApk(data.generalData.applicationName, File(data.generalData.apkDirectory), targetUri)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(appDetailRequest: AppDetailRequest): AppDetailFragmentViewModel
    }
}