package sk.styk.martin.apkanalyzer.ui.activity.appdetail.actions

import android.content.Context
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.ui.customview.SpeedDialMenuAdapter
import sk.styk.martin.apkanalyzer.ui.customview.SpeedDialMenuItem
import java.lang.ref.WeakReference

abstract class AppActionsSpeedMenu(actionPresenter: AppActionsContract.Presenter) : SpeedDialMenuAdapter() {

    private val presenterRef: WeakReference<AppActionsContract.Presenter> = WeakReference(actionPresenter)

    abstract val menuItems: List<AppActions>

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
        AppActions.SHARE -> SpeedDialMenuItem( R.drawable.ic_share, R.string.share_apk)
        AppActions.SAVE_ICON -> SpeedDialMenuItem(R.drawable.ic_image, R.string.save_icon)
        AppActions.SHOW_MANIFEST -> SpeedDialMenuItem(R.drawable.ic_file, R.string.show_manifest)
        AppActions.OPEN_PLAY -> SpeedDialMenuItem(R.drawable.ic_google_play, R.string.show_app_google_play)
        AppActions.BUILD_INFO -> SpeedDialMenuItem(R.drawable.ic_info_white, R.string.show_app_system_page)
        AppActions.SHOW_MORE -> SpeedDialMenuItem( R.drawable.ic_menu_dots, R.string.show_more)
    }

    override fun onMenuItemClick(position: Int): Boolean = presenterRef.get()?.let { presenter ->
        when (menuItems[position]) {
            AppActions.INSTALL -> presenter.installAppClick()
            AppActions.COPY -> presenter.exportClick()
            AppActions.SHARE -> presenter.shareClick()
            AppActions.SAVE_ICON -> presenter.saveIconClick()
            AppActions.SHOW_MANIFEST -> presenter.showManifestClick()
            AppActions.OPEN_PLAY -> presenter.showGooglePlayClick()
            AppActions.BUILD_INFO -> presenter.showSystemPageClick()
            AppActions.SHOW_MORE -> presenter.showMoreClick()
        }
        true
    } ?: false

    @ColorInt
    override fun getBackgroundColour(position: Int, context: Context) = ContextCompat.getColor(context, R.color.accentLight)


    override fun fabRotationDegrees(): Float = 90F
}
