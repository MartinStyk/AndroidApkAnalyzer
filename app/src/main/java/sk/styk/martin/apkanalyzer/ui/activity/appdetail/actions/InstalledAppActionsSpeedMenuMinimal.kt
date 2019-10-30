package sk.styk.martin.apkanalyzer.ui.activity.appdetail.actions

class InstalledAppActionsSpeedMenuMinimal(actionPresenter: AppActionsContract.Presenter) : AppActionsSpeedMenu(actionPresenter) {

    override val menuItems = listOf(
            AppActions.SHOW_MORE,
            AppActions.COPY,
            AppActions.SHOW_MANIFEST)
}
