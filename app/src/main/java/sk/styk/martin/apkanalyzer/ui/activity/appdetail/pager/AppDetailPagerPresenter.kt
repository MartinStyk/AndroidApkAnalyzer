package sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager

import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import sk.styk.martin.apkanalyzer.business.analysis.task.AppDetailLoader
import sk.styk.martin.apkanalyzer.business.upload.task.AppDataUploadTask
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager.AppDetailPagerContract.Companion.ARG_PACKAGE_NAME
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager.AppDetailPagerContract.Companion.ARG_PACKAGE_PATH


/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
class AppDetailPagerPresenter(
        val loader: Loader<AppDetailData?>,
        val loaderManager: LoaderManager
) : LoaderManager.LoaderCallbacks<AppDetailData?>, AppDetailPagerContract.Presenter {

    override lateinit var view: AppDetailPagerContract.View

    private var packageName: String? = null
    private var pathToPackage: String? = null

    private var appDetailData: AppDetailData? = null

    override fun initialize(bundle: Bundle) {
        packageName = bundle.getString(ARG_PACKAGE_NAME)
        pathToPackage = bundle.getString(ARG_PACKAGE_PATH)
        view.setUpViews()
        loaderManager.initLoader(AppDetailLoader.ID, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<AppDetailData?> {
        return loader
    }

    override fun onLoadFinished(loader: Loader<AppDetailData?>, appDetailData: AppDetailData?) {
        this.appDetailData = appDetailData
        view.hideLoading()

        if (appDetailData == null) {
            view.showLoadingFailed()

        } else {
            view.showAppDetails(packageName = appDetailData.generalData.packageName, icon = appDetailData.generalData.icon)

//            Temporary disable uploads
//            AppDataUploadTask().execute(appDetailData)
        }
    }

    override fun onLoaderReset(loader: Loader<AppDetailData?>) {
        appDetailData = null
    }

    override fun actionButtonClick() {
        // show actions only when data is loaded
        appDetailData?.let {
            view.showActionDialog(it)
        }
    }

    override fun getData(): AppDetailData? {
        return appDetailData
    }

}
