package sk.styk.martin.apkanalyzer.ui.activity.appdetail.actions

/**
 * @author Martin Styk {@literal <martin.styk@gmail.com>}
 */
class InstalledAppActionsSpeedMenu(actionPresenter: AppActionsContract.Presenter) : AppActionsSpeedMenu(actionPresenter) {

    override val menuItems = listOf(
            AppActions.OPEN_PLAY,
            AppActions.BUILD_INFO,
            AppActions.SAVE_ICON,
            AppActions.SHARE,
            AppActions.COPY,
            AppActions.SHOW_MANIFEST)
}
