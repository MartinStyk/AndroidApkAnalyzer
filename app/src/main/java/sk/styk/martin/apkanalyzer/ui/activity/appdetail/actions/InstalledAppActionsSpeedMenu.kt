package sk.styk.martin.apkanalyzer.ui.activity.appdetail.actions

class InstalledAppActionsSpeedMenu(actionPresenter: AppActionsContract.Presenter) : AppActionsSpeedMenu(actionPresenter) {

    companion object{
        const val REQUIRED_HEIGHT_DP = 420
    }

    override val menuItems = listOf(
            AppActions.OPEN_PLAY,
            AppActions.BUILD_INFO,
            AppActions.SAVE_ICON,
            AppActions.SHARE,
            AppActions.COPY,
            AppActions.SHOW_MANIFEST)
}
