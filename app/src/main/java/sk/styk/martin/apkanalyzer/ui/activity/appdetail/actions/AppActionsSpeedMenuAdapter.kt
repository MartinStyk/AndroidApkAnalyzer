package sk.styk.martin.apkanalyzer.ui.activity.appdetail.actions

import android.content.Context
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.views.SpeedDialMenuAdapter
import sk.styk.martin.apkanalyzer.views.SpeedDialMenuItem
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class AppActionsSpeedMenuAdapter @Inject constructor() : SpeedDialMenuAdapter() {

    var menuItems: List<AppActions> = emptyList()

    private val installAppChannel = BroadcastChannel<Unit>(Channel.CONFLATED)
    val installApp = installAppChannel.asFlow()

    private val exportAppChannel = BroadcastChannel<Unit>(Channel.CONFLATED)
    val exportApp = exportAppChannel.asFlow()

    private val shareAppChannel = BroadcastChannel<Unit>(Channel.CONFLATED)
    val shareApp = shareAppChannel.asFlow()

    private val saveIconChannel = BroadcastChannel<Unit>(Channel.CONFLATED)
    val saveIcon = saveIconChannel.asFlow()

    private val showManifestChannel = BroadcastChannel<Unit>(Channel.CONFLATED)
    val showManifest = showManifestChannel.asFlow()

    private val openGooglePlayChannel = BroadcastChannel<Unit>(Channel.CONFLATED)
    val openGooglePlay = openGooglePlayChannel.asFlow()

    private val openSystemInfoChannel = BroadcastChannel<Unit>(Channel.CONFLATED)
    val openSystemInfo = openSystemInfoChannel.asFlow()

    private val showMoreChannel = BroadcastChannel<Unit>(Channel.CONFLATED)
    val showMore = showMoreChannel.asFlow()

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
        when (menuItems[position]) {
            AppActions.INSTALL -> installAppChannel.offer(Unit)
            AppActions.COPY -> exportAppChannel.offer(Unit)
            AppActions.SHARE -> shareAppChannel.offer(Unit)
            AppActions.SAVE_ICON -> saveIconChannel.offer(Unit)
            AppActions.SHOW_MANIFEST -> showManifestChannel.offer(Unit)
            AppActions.OPEN_PLAY -> openGooglePlayChannel.offer(Unit)
            AppActions.BUILD_INFO -> openSystemInfoChannel.offer(Unit)
            AppActions.SHOW_MORE -> showMoreChannel.offer(Unit)
        }
        return true
    }

    @ColorInt
    override fun getBackgroundColour(position: Int, context: Context) = ContextCompat.getColor(context, R.color.accentLight)


    override fun fabRotationDegrees(): Float = 90F
}
