package sk.styk.martin.apkanalyzer.ui.activity.applist

import android.os.Bundle
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import sk.styk.martin.apkanalyzer.business.analysis.task.AppListFromPackageNamesLoader
import sk.styk.martin.apkanalyzer.business.base.task.ApkAnalyzerAbstractAsyncLoader
import sk.styk.martin.apkanalyzer.model.list.AppListData
import sk.styk.martin.apkanalyzer.ui.activity.applist.AppListContract.Companion.PACKAGES_ARGUMENT

class AppListPresenter(
        private val loader: ApkAnalyzerAbstractAsyncLoader<List<AppListData>>,
        private val loaderManager: LoaderManager
) :
        LoaderManager.LoaderCallbacks<List<AppListData>>, AppListContract.Presenter {

    override lateinit var view: AppListContract.View
    private var packageNames: List<String> = emptyList()
    private var appData: List<AppListData> = emptyList()


    override fun initialize(bundle: Bundle) {
        packageNames = bundle.getStringArrayList(PACKAGES_ARGUMENT) ?: throw IllegalArgumentException()
        startLoadingData()
    }

    private fun startLoadingData() {
        loaderManager.initLoader(AppListFromPackageNamesLoader.ID, null, this)
    }

    override fun onCreateLoader(bunid: Int, args: Bundle?): Loader<List<AppListData>> {
        return loader
    }

    override fun onLoadFinished(loader: Loader<List<AppListData>>, data: List<AppListData>) {
        view.setUpViews()

        appData = data

        view.hideLoading()
        if (data.isEmpty())
            view.nothingToDisplay()
        else
            view.showAppList()
    }

    override fun onLoaderReset(loader: Loader<List<AppListData>>) {}

    override fun itemCount(): Int = appData.size

    override fun onAppClick(app: AppListData) = view.openAppDetailActivity(packageName = app.packageName)

    override fun onBindViewOnPosition(position: Int, holder: AppListContract.ItemView) {
        return holder.bind(appData[position])
    }

}