package sk.styk.martin.apkanalyzer.ui.appdetail

import android.Manifest
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Environment
import androidx.lifecycle.*
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.business.analysis.logic.launcher.AppDetailDataManager
import sk.styk.martin.apkanalyzer.manager.permission.PermissionManager
import sk.styk.martin.apkanalyzer.manager.resources.ResourcesManager
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.manifest.ManifestActivity
import sk.styk.martin.apkanalyzer.util.ColorInfo
import sk.styk.martin.apkanalyzer.util.TextInfo
import sk.styk.martin.apkanalyzer.util.components.SnackBarComponent
import sk.styk.martin.apkanalyzer.util.coroutines.DispatcherProvider
import sk.styk.martin.apkanalyzer.util.file.FileUtils
import sk.styk.martin.apkanalyzer.util.file.toBitmap
import sk.styk.martin.apkanalyzer.util.live.SingleLiveEvent
import java.io.File
import java.io.IOException
import kotlin.math.abs

internal const val LOADING_STATE = 0
internal const val ERROR_STATE = 1
internal const val DATA_STATE = 2

@OptIn(ExperimentalCoroutinesApi::class)
class AppDetailFragmentViewModel @AssistedInject constructor(
        @Assisted val appDetailRequest: AppDetailRequest,
        private val dispatcherProvider: DispatcherProvider,
        private val appDetailDataManager: AppDetailDataManager,
        private val resourcesManager: ResourcesManager,
        private val permissionManager: PermissionManager,
        private val appActionsAdapter: AppActionsSpeedMenuAdapter,
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

    private val exportAppEvent = SingleLiveEvent<Unit>()
    val exportApp: LiveData<Unit> = exportAppEvent

    private val shareAppEvent = SingleLiveEvent<String>()
    val shareApp: LiveData<String> = shareAppEvent

    private val openImageEvent = SingleLiveEvent<String>()
    val openImage: LiveData<String> = openImageEvent

    private val showManifestEvent = SingleLiveEvent<ManifestActivity.ManifestRequest>()
    val showManifest: LiveData<ManifestActivity.ManifestRequest> = showManifestEvent

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

    private val accentColorLiveData = MutableLiveData<Int>(resourcesManager.getColor(ColorInfo.fromColor(R.color.secondary)))
    val accentColor: LiveData<Int> = accentColorLiveData

    val toolbarBackground: LiveData<Drawable> = accentColor.map { GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(it, Color.TRANSPARENT)) }
    val toolbarIcon: LiveData<Drawable> = appDetails.map { it.generalData.icon!! }

    init {
        viewModelScope.launch {
            try {
                val detail = withContext(dispatcherProvider.default()) {
                    when (appDetailRequest) {
                        is AppDetailRequest.InstalledPackage -> appDetailDataManager.loadForInstalledPackage(appDetailRequest.packageName)
                        is AppDetailRequest.ExternalPackage -> appDetailDataManager.loadForExternalPackage(appDetailRequest.packageUri)
                    }
                }
                appDetailsLiveData.value = detail
                viewStateLiveData.value = DATA_STATE
                actionButtonVisibilityLiveData.value = true
                setupToolbar(detail)
            } catch (e: Exception) {
                viewStateLiveData.value = ERROR_STATE
                actionButtonVisibilityLiveData.value = false
            }
        }
        observeApkActions()
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        updateActionButtonAdapter()
    }

    private fun setupToolbar(detail: AppDetailData) = viewModelScope.launch(dispatcherProvider.main()) {
        val palette = resourcesManager.generatePalette(detail.generalData.icon!!)
        val accentColor = palette.getDarkVibrantColor(resourcesManager.getColor(R.color.secondary))
        accentColorLiveData.postValue(accentColor)
    }

    override fun onOffsetChanged(bar: AppBarLayout, verticalOffset: Int) {
        actionButtonVisibilityLiveData.value = (abs(verticalOffset) - bar.totalScrollRange != 0) && viewState.value == DATA_STATE
    }

    private fun updateActionButtonAdapter() {
        val displayHeight = resourcesManager.getDisplayHeight()
        appActionsAdapter.menuItems = when (appDetailRequest) {
            is AppDetailRequest.ExternalPackage ->
                if (displayHeight < 420) {
                    listOf(AppActionsSpeedMenuAdapter.AppActions.SHOW_MORE, AppActionsSpeedMenuAdapter.AppActions.SHOW_MANIFEST, AppActionsSpeedMenuAdapter.AppActions.INSTALL)
                } else {
                    listOf(AppActionsSpeedMenuAdapter.AppActions.OPEN_PLAY, AppActionsSpeedMenuAdapter.AppActions.SAVE_ICON, AppActionsSpeedMenuAdapter.AppActions.SHARE,
                            AppActionsSpeedMenuAdapter.AppActions.COPY, AppActionsSpeedMenuAdapter.AppActions.SHOW_MANIFEST, AppActionsSpeedMenuAdapter.AppActions.INSTALL)
                }
            is AppDetailRequest.InstalledPackage ->
                if (displayHeight < 420) {
                    listOf(AppActionsSpeedMenuAdapter.AppActions.SHOW_MORE, AppActionsSpeedMenuAdapter.AppActions.COPY, AppActionsSpeedMenuAdapter.AppActions.SHOW_MANIFEST)
                } else {
                    listOf(AppActionsSpeedMenuAdapter.AppActions.OPEN_PLAY, AppActionsSpeedMenuAdapter.AppActions.BUILD_INFO, AppActionsSpeedMenuAdapter.AppActions.SAVE_ICON,
                            AppActionsSpeedMenuAdapter.AppActions.SHARE, AppActionsSpeedMenuAdapter.AppActions.COPY, AppActionsSpeedMenuAdapter.AppActions.SHOW_MANIFEST)
                }
        }
        actionButtonAdapterLiveData.value = appActionsAdapter
    }

    private fun observeApkActions() {
        viewModelScope.launch {
            appActionsAdapter.installApp.collect {
                appDetails.value?.let {
                    installAppEvent.value = it.generalData.apkDirectory
                }
            }
        }
        viewModelScope.launch {
            appActionsAdapter.exportApp.collect { }
        }
        viewModelScope.launch {
            appActionsAdapter.shareApp.collect {
                appDetails.value?.let {
                    shareAppEvent.value = it.generalData.apkDirectory
                }
            }
        }
        viewModelScope.launch {
            appActionsAdapter.saveIcon.collect {
                appDetails.value?.let { data ->
                    if (permissionManager.hasPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        saveImage()
                    } else {
                        permissionManager.requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, object : PermissionManager.PermissionCallback {
                            override fun onPermissionDenied(permission: String) {
                                showSnackEvent.value = SnackBarComponent(TextInfo.from(R.string.permission_not_granted), Snackbar.LENGTH_LONG)
                            }

                            override fun onPermissionGranted(permission: String) {
                                viewModelScope.launch { saveImage() }
                            }
                        })
                    }
                }
            }
        }
        viewModelScope.launch {
            appActionsAdapter.showManifest.collect {
                appDetails.value?.let {
                    showManifestEvent.value = ManifestActivity.ManifestRequest(
                            packageName = it.generalData.packageName,
                            apkDirectory = it.generalData.apkDirectory,
                            versionName = it.generalData.versionName,
                            versionCode = it.generalData.versionCode)
                }
            }
        }
        viewModelScope.launch {
            appActionsAdapter.openGooglePlay.collect {
                appDetails.value?.let {
                    openGooglePlayEvent.value = it.generalData.packageName
                }
            }
        }
        viewModelScope.launch {
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

    private suspend fun saveImage() {
        val data = appDetails.value ?: return
        val target = File(Environment.getExternalStorageDirectory(), "${data.generalData.packageName}_${data.generalData.versionName}_${data.generalData.versionCode}_icon.png")
        val icon = data.generalData.icon?.toBitmap()
        if (icon != null) {
            viewModelScope.launch {
                withContext(dispatcherProvider.io()) {
                    try {
                        FileUtils.writeBitmap(icon, target.absolutePath)
                    } catch (e: IOException) {
                    }
                }
                showSnackEvent.value = SnackBarComponent(TextInfo.from(R.string.icon_saved),
                        action = TextInfo.from(R.string.action_show),
                        callback = {
                            openImageEvent.value = target.absolutePath
                        })
            }
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(appDetailRequest: AppDetailRequest): AppDetailFragmentViewModel
    }
}