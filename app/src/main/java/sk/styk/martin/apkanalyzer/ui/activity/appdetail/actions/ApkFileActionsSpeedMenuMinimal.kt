package sk.styk.martin.apkanalyzer.ui.activity.appdetail.actions

class ApkFileActionsSpeedMenuMinimal(actionPresenter: AppActionsContract.Presenter) : AppActionsSpeedMenu(actionPresenter) {

    override val menuItems = listOf(
            AppActions.SHOW_MORE,
            AppActions.SHOW_MANIFEST,
            AppActions.INSTALL)
}
