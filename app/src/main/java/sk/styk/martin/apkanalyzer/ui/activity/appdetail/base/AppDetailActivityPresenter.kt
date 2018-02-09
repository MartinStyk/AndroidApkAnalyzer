package sk.styk.martin.apkanalyzer.ui.activity.appdetail.base

/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
class AppDetailActivityPresenter : AppDetailActivityContract.Presenter {

    override lateinit var view: AppDetailActivityContract.View

    override fun initialize() {
        view.setupViews()
    }
}