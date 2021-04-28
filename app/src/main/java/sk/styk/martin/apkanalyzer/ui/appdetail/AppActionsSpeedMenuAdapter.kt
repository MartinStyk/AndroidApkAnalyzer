package sk.styk.martin.apkanalyzer.ui.appdetail

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.views.SpeedDialMenuAdapter
import sk.styk.martin.apkanalyzer.views.SpeedDialMenuItem
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class AppActionsSpeedMenuAdapter @Inject constructor() : SpeedDialMenuAdapter() {

    var menuItems: List<AppActions> = emptyList()

    private val installAppFlow = MutableSharedFlow<Unit>()
    val installApp: Flow<Unit>  = installAppFlow

    private val exportAppFlow = MutableSharedFlow<Unit>()
    val exportApp: Flow<Unit>  = exportAppFlow

    private val saveIconFlow = MutableSharedFlow<Unit>()
    val saveIcon: Flow<Unit>  = saveIconFlow

    private val showManifestFlow = MutableSharedFlow<Unit>()
    val showManifest: Flow<Unit>  = showManifestFlow

    private val openGooglePlayFlow = MutableSharedFlow<Unit>()
    val openGooglePlay: Flow<Unit>  = openGooglePlayFlow

    private val openSystemInfoFlow = MutableSharedFlow<Unit>()
    val openSystemInfo: Flow<Unit> = openSystemInfoFlow

    enum class AppActions {
        INSTALL,
        EXPORT_APK,
        SAVE_ICON,
        SHOW_MANIFEST,
        OPEN_PLAY,
        BUILD_INFO,
    }

    override fun getCount(): Int = menuItems.size

    override fun getMenuItem(position: Int): SpeedDialMenuItem = when (menuItems[position]) {
        AppActions.INSTALL -> SpeedDialMenuItem(R.drawable.ic_android, R.string.install_app)
        AppActions.EXPORT_APK -> SpeedDialMenuItem(R.drawable.ic_save, R.string.copy_apk)
        AppActions.SAVE_ICON -> SpeedDialMenuItem(R.drawable.ic_image, R.string.save_icon)
        AppActions.SHOW_MANIFEST -> SpeedDialMenuItem(R.drawable.ic_file, R.string.show_manifest)
        AppActions.OPEN_PLAY -> SpeedDialMenuItem(R.drawable.ic_google_play, R.string.show_app_google_play)
        AppActions.BUILD_INFO -> SpeedDialMenuItem(R.drawable.ic_info_white, R.string.show_app_system_page)
    }

    override fun onMenuItemClick(position: Int): Boolean {
        GlobalScope.launch {
            when (menuItems[position]) {
                AppActions.INSTALL -> installAppFlow.emit(Unit)
                AppActions.EXPORT_APK -> exportAppFlow.emit(Unit)
                AppActions.SAVE_ICON -> saveIconFlow.emit(Unit)
                AppActions.SHOW_MANIFEST -> showManifestFlow.emit(Unit)
                AppActions.OPEN_PLAY -> openGooglePlayFlow.emit(Unit)
                AppActions.BUILD_INFO -> openSystemInfoFlow.emit(Unit)
            }
        }
        return true
    }

    override fun fabRotationDegrees(): Float = 90F
}
