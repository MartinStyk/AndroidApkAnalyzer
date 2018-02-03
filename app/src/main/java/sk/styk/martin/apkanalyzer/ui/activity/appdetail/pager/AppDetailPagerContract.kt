package sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager

import android.graphics.drawable.Drawable
import android.os.Bundle
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.ui.base.BasePresenter

/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
interface AppDetailPagerContract {
    interface View {
        fun setUpViews()

        fun hideLoading()

        fun showLoadingFailed()

        fun showAppDetails(packageName: String, icon: Drawable?)

        fun showActionDialog(data: AppDetailData)
    }

    interface Presenter : BasePresenter<View> {
        fun initialize(bundle: Bundle)

        fun actionButtonClick()

        fun getData(): AppDetailData?
    }

    companion object {
        const val ARG_PAGER_PAGE = "dataForChild"
        const val ARG_PACKAGE_NAME = "packageName"
        const val ARG_PACKAGE_PATH = "packagePath"
    }
}