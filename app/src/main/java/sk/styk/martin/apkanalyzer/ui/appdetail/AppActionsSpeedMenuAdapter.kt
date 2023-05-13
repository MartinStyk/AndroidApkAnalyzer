package sk.styk.martin.apkanalyzer.ui.appdetail

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.views.SpeedDialMenuAdapter
import sk.styk.martin.apkanalyzer.views.SpeedDialMenuItem
import javax.inject.Inject

class AppActionsSpeedMenuAdapter @Inject constructor() : SpeedDialMenuAdapter() {

    var menuItems: List<AppActions> = emptyList()

    private val exportAppFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val exportApp: Flow<Unit> = exportAppFlow

    private val saveIconFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val saveIcon: Flow<Unit> = saveIconFlow

    private val showManifestFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val showManifest: Flow<Unit> = showManifestFlow

    private val openGooglePlayFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val openGooglePlay: Flow<Unit> = openGooglePlayFlow

    private val openSystemInfoFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val openSystemInfo: Flow<Unit> = openSystemInfoFlow

    enum class AppActions {
        EXPORT_APK,
        SAVE_ICON,
        SHOW_MANIFEST,
        OPEN_PLAY,
        BUILD_INFO,
    }

    override fun getCount(): Int = menuItems.size

    override fun getMenuItem(position: Int): SpeedDialMenuItem = when (menuItems[position]) {
        AppActions.EXPORT_APK -> SpeedDialMenuItem(R.drawable.ic_save, R.string.copy_apk)
        AppActions.SAVE_ICON -> SpeedDialMenuItem(R.drawable.ic_image, R.string.save_icon)
        AppActions.SHOW_MANIFEST -> SpeedDialMenuItem(R.drawable.ic_file, R.string.show_manifest)
        AppActions.OPEN_PLAY -> SpeedDialMenuItem(R.drawable.ic_google_play, R.string.show_app_google_play)
        AppActions.BUILD_INFO -> SpeedDialMenuItem(R.drawable.ic_info_white, R.string.show_app_system_page)
    }

    override fun onMenuItemClick(position: Int): Boolean {
        when (menuItems[position]) {
            AppActions.EXPORT_APK -> exportAppFlow.tryEmit(Unit)
            AppActions.SAVE_ICON -> saveIconFlow.tryEmit(Unit)
            AppActions.SHOW_MANIFEST -> showManifestFlow.tryEmit(Unit)
            AppActions.OPEN_PLAY -> openGooglePlayFlow.tryEmit(Unit)
            AppActions.BUILD_INFO -> openSystemInfoFlow.tryEmit(Unit)
        }
        return true
    }

    override fun fabRotationDegrees(): Float = 90F
}
