package sk.styk.martin.apkanalyzer.ui.activity.appdetail.base

import sk.styk.martin.apkanalyzer.ui.base.BasePresenter

interface AppDetailActivityContract {
    interface View {
        fun setupViews()
    }

    interface Presenter : BasePresenter<View>
}