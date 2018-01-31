package sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.ui.BasePresenter

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

        fun getGeneralDetailsFragment(): Fragment
        fun getCertificateDetailsFragment(): Fragment
        fun getResourceDetailsFragment(): Fragment
        fun getActivityDetailsFragment(): Fragment
        fun getServiceDetailsFragment(): Fragment
        fun getProviderDetailsFragment(): Fragment
        fun getReceiverDetailsFragment(): Fragment
        fun getFeatureDetailsFragment(): Fragment
        fun getUsedPermissionDetailsFragment(): Fragment
        fun getDefinedPermissionDetailsFragment(): Fragment
        fun getClasspathDetailsFragment(): Fragment
    }

    companion object {
        const val ARG_PAGER_PAGE = "dataForChild"
        const val ARG_PACKAGE_NAME = "packageName"
        const val ARG_PACKAGE_PATH = "packagePath"
    }
}