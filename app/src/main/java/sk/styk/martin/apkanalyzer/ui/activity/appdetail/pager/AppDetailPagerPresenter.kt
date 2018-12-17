package sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager

import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import sk.styk.martin.apkanalyzer.business.analysis.task.AppDetailLoader
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager.AppDetailPagerContract.Companion.ARG_PACKAGE_NAME
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager.AppDetailPagerContract.Companion.ARG_PACKAGE_PATH
import sk.styk.martin.apkanalyzer.util.AppDetailDataExchange


/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
class AppDetailPagerPresenter(
        val loader: Loader<String?>,
        val loaderManager: LoaderManager
) : LoaderManager.LoaderCallbacks<String?>, AppDetailPagerContract.Presenter {

    override lateinit var view: AppDetailPagerContract.View

    override var packageName: String? = null
    private var pathToPackage: String? = null

    private var appDetailData: AppDetailData? = null

    override fun initialize(bundle: Bundle) {
        packageName = bundle.getString(ARG_PACKAGE_NAME)
        pathToPackage = bundle.getString(ARG_PACKAGE_PATH)
        view.setUpViews()
        loaderManager.initLoader(AppDetailLoader.ID, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<String?> {
        return loader
    }

    override fun onLoadFinished(loader: Loader<String?>, packageName: String?) {
        view.hideLoading()

        if (packageName == null) {
            view.showLoadingFailed()

        } else {
            this.packageName = packageName
            AppDetailDataExchange.get(packageName)?.let {
                appDetailData = it
                view.showAppDetails(packageName = it.generalData.packageName, icon = it.generalData.icon)
            }
//            Temporary disable uploads
//            AppDataUploadTask().execute(appDetailData)
        }
    }

    override fun onLoaderReset(loader: Loader<String?>) {
        appDetailData = null
    }

    override fun actionButtonClick() {
        // show actions only when data is loaded
        appDetailData?.let {
            view.showActionDialog(it)
        }
    }

}
