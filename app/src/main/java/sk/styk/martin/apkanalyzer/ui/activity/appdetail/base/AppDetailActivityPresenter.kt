package sk.styk.martin.apkanalyzer.ui.activity.appdetail.base

class AppDetailActivityPresenter : AppDetailActivityContract.Presenter {

    override lateinit var view: AppDetailActivityContract.View

    override fun initialize() {
        view.setupViews()
    }
}