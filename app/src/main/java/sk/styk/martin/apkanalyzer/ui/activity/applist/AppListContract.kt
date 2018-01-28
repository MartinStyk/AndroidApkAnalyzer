package sk.styk.martin.apkanalyzer.ui.activity.applist

import android.os.Bundle
import sk.styk.martin.apkanalyzer.model.list.AppListData
import sk.styk.martin.apkanalyzer.ui.BasePresenter

/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
interface AppListContract {
    interface View {
        fun setUpViews()

        fun hideLoading()

        fun nothingToDisplay()

        fun showAppList()

        fun openAppDetailActivity(packageName: String)
    }

    interface ItemView {
        fun bind(appData: AppListData)
    }

    interface Presenter : BasePresenter {
        fun initialize(bundle: Bundle)

        fun appCount(): Int

        fun onAppClick(position: Int)

        fun onBindAppViewOnPosition(position: Int, holder: ItemView)
    }

    companion object {
        const val PACKAGES_ARGUMENT = "args_package_names"
    }
}