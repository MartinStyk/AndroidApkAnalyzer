package sk.styk.martin.apkanalyzer.ui.activity.localstatistics

import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import sk.styk.martin.apkanalyzer.business.analysis.task.LocalStatisticsLoader
import sk.styk.martin.apkanalyzer.business.base.task.ApkAnalyzerAbstractAsyncLoader
import sk.styk.martin.apkanalyzer.model.detail.AppSource
import sk.styk.martin.apkanalyzer.model.statistics.LocalStatisticsDataWithCharts


/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
class LocalStatisticsPresenter(
        private val loader: ApkAnalyzerAbstractAsyncLoader<LocalStatisticsDataWithCharts>,
        private val loaderManager: LoaderManager
) : LocalStatisticsContract.Presenter,
        LoaderManager.LoaderCallbacks<LocalStatisticsDataWithCharts>, LocalStatisticsLoader.ProgressCallback {

    override lateinit var view: LocalStatisticsContract.View
    var data: LocalStatisticsDataWithCharts? = null

    /**
     * Initializes tihe presenter by showng/hiding proper views and starting data loading.
     */
    override fun initialize() {
        startLoadingData()
        view.setupCharts()
    }

    override fun onProgressChanged(currentProgress: Int, maxProgress: Int) {
        view.changeProgress(currentProgress, maxProgress)
    }

    override fun onMinSdkValueSelected(label: String) {
        data?.let {
            val sdkInteger = Integer.valueOf(label)
            view.showAppLists(it.statisticsData.minSdk[sdkInteger] ?: return)
        }
    }

    override fun onTargetSdkValueSelected(label: String) {
        data?.let {
            val sdkInteger = Integer.valueOf(label)
            view.showAppLists(it.statisticsData.targetSdk[sdkInteger] ?: return)
        }
    }

    override fun onInstallLocationValueSelected(label: String) {
        data?.let {
            view.showAppLists(it.statisticsData.installLocation[label] ?: return)
        }
    }

    override fun onSignAlgorithmValueSelected(label: String) {
        data?.let {
            view.showAppLists(it.statisticsData.signAlgorithm[label] ?: return)
        }
    }

    override fun onAppSourceValueSelected(label: String) {
        data?.let {
            view.showAppLists(it.statisticsData.appSource[AppSource.Companion.valueOf(label)] ?: return)
        }
    }

    override fun onAppSourceValueSelected(apps: List<String>) {
        view.showAppLists(apps)
    }


    // Data loading part
    private fun startLoadingData() {
        loaderManager.initLoader(LocalStatisticsLoader.ID, null, this)
        (loader as LocalStatisticsLoader).setCallbackReference(this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<LocalStatisticsDataWithCharts> {
        return loader
    }

    override fun onLoadFinished(loader: Loader<LocalStatisticsDataWithCharts>, result: LocalStatisticsDataWithCharts) {
        data = result
        view.showStatistics(result)
    }

    override fun onLoaderReset(loader: Loader<LocalStatisticsDataWithCharts>) {
        data = null
    }
}