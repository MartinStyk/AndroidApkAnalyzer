package sk.styk.martin.apkanalyzer.ui.appdetail

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
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
    val installApp = installAppFlow

    private val exportAppFlow = MutableSharedFlow<Unit>()
    val exportApp = exportAppFlow

    private val shareAppFlow = MutableSharedFlow<Unit>()
    val shareApp = shareAppFlow

    private val saveIconFlow = MutableSharedFlow<Unit>()
    val saveIcon = saveIconFlow

    private val showManifestFlow = MutableSharedFlow<Unit>()
    val showManifest = showManifestFlow

    private val openGooglePlayFlow = MutableSharedFlow<Unit>()
    val openGooglePlay = openGooglePlayFlow

    private val openSystemInfoFlow = MutableSharedFlow<Unit>()
    val openSystemInfo = openSystemInfoFlow

    private val showMoreFlow = MutableSharedFlow<Unit>()
    val showMore = showMoreFlow

    enum class AppActions {
        INSTALL,
        COPY,
        SHARE,
        SAVE_ICON,
        SHOW_MANIFEST,
        OPEN_PLAY,
        BUILD_INFO,
        SHOW_MORE
    }

    override fun getCount(): Int = menuItems.size

    override fun getMenuItem(position: Int): SpeedDialMenuItem = when (menuItems[position]) {
        AppActions.INSTALL -> SpeedDialMenuItem(R.drawable.ic_android, R.string.install_app)
        AppActions.COPY -> SpeedDialMenuItem(R.drawable.ic_save, R.string.copy_apk)
        AppActions.SHARE -> SpeedDialMenuItem(R.drawable.ic_share, R.string.share_apk)
        AppActions.SAVE_ICON -> SpeedDialMenuItem(R.drawable.ic_image, R.string.save_icon)
        AppActions.SHOW_MANIFEST -> SpeedDialMenuItem(R.drawable.ic_file, R.string.show_manifest)
        AppActions.OPEN_PLAY -> SpeedDialMenuItem(R.drawable.ic_google_play, R.string.show_app_google_play)
        AppActions.BUILD_INFO -> SpeedDialMenuItem(R.drawable.ic_info_white, R.string.show_app_system_page)
        AppActions.SHOW_MORE -> SpeedDialMenuItem(R.drawable.ic_menu_dots, R.string.show_more)
    }

    override fun onMenuItemClick(position: Int): Boolean {
        GlobalScope.launch {
            when (menuItems[position]) {
                AppActions.INSTALL -> installAppFlow.emit(Unit)
                AppActions.COPY -> exportAppFlow.emit(Unit)
                AppActions.SHARE -> shareAppFlow.emit(Unit)
                AppActions.SAVE_ICON -> saveIconFlow.emit(Unit)
                AppActions.SHOW_MANIFEST -> showManifestFlow.emit(Unit)
                AppActions.OPEN_PLAY -> openGooglePlayFlow.emit(Unit)
                AppActions.BUILD_INFO -> openSystemInfoFlow.emit(Unit)
                AppActions.SHOW_MORE -> showMoreFlow.emit(Unit)
            }
        }
        return true
    }

    override fun fabRotationDegrees(): Float = 90F
}
