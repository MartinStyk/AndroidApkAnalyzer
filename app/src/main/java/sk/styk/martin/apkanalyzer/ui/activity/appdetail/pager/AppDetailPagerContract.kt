package sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager

import android.graphics.drawable.Drawable
import android.os.Bundle
import sk.styk.martin.apkanalyzer.ui.base.BasePresenter

interface AppDetailPagerContract {
    interface View {
        fun setUpViews()

        fun hideLoading()

        fun showLoadingFailed()

        fun showAppDetails(packageName: String, icon: Drawable?)
    }

    interface Presenter : BasePresenter<View> {
        var packageName: String?

        fun initialize(bundle: Bundle)
    }

    companion object {
        const val ARG_PACKAGE_NAME = "packageName"
        const val ARG_PACKAGE_PATH = "packagePath"
    }
}