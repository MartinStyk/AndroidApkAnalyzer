package sk.styk.martin.apkanalyzer.ui.activity.appdetail.actions

/**
 * @author Martin Styk {@literal <martin.styk@gmail.com>}
 */
class InstalledAppActionsSpeedMenuMinimal(actionPresenter: AppActionsContract.Presenter) : AppActionsSpeedMenu(actionPresenter) {

    override val menuItems = listOf(
            AppActions.SHOW_MORE,
            AppActions.COPY,
            AppActions.SHOW_MANIFEST)
}
