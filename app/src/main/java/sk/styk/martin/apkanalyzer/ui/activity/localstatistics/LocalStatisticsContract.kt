package sk.styk.martin.apkanalyzer.ui.activity.localstatistics

import sk.styk.martin.apkanalyzer.model.statistics.LocalStatisticsDataWithCharts
import sk.styk.martin.apkanalyzer.ui.BasePresenter

/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
interface LocalStatisticsContract {
    interface View {

        fun changeProgress(currentProgress: Int, maxProgress: Int)

        fun setupCharts()

        fun showStatistics(data: LocalStatisticsDataWithCharts)

        fun showAppLists(packages: List<String>)
    }

    interface Presenter : BasePresenter<View> {
        fun onMinSdkValueSelected(label: String)
        fun onTargetSdkValueSelected(label: String)
        fun onInstallLocationValueSelected(label: String)
        fun onSignAlgorithmValueSelected(label: String)
        fun onAppSourceValueSelected(label: String)
    }
}