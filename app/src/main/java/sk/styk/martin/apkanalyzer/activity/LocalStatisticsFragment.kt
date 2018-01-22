package sk.styk.martin.apkanalyzer.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_local_statistics.*
import lecho.lib.hellocharts.gesture.ContainerScrollType
import lecho.lib.hellocharts.gesture.ZoomType
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener
import lecho.lib.hellocharts.model.SubcolumnValue
import lecho.lib.hellocharts.view.ColumnChartView
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.activity.dialog.AppListDialog
import sk.styk.martin.apkanalyzer.business.task.LocalStatisticsLoader
import sk.styk.martin.apkanalyzer.model.statistics.LocalStatisticsDataWithCharts
import sk.styk.martin.apkanalyzer.util.BigDecimalFormatter
import java.util.*

/**
 * @author Martin Styk
 */
class LocalStatisticsFragment : Fragment(), LoaderManager.LoaderCallbacks<LocalStatisticsDataWithCharts>, LocalStatisticsLoader.ProgressCallback {

    private var data: LocalStatisticsDataWithCharts? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_local_statistics, container, false)

        // We need to re-set callback of loader in case of configuration change
        val loader = loaderManager.initLoader(LocalStatisticsLoader.ID, null, this) as LocalStatisticsLoader
        loader.setCallbackReference(this)

        setHasOptionsMenu(true)

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupChart(chart_min_sdk)
        setupChart(chart_target_sdk)
        setupChart(chart_install_location)
        setupChart(chart_sign_algorithm)
        setupChart(chart_app_source)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        // Hide action bar item for searching
        menu?.clear()

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<LocalStatisticsDataWithCharts> {
        return LocalStatisticsLoader(context, this)
    }

    override fun onLoadFinished(loader: Loader<LocalStatisticsDataWithCharts>, data: LocalStatisticsDataWithCharts) {
        this.data = data
        val stats = data.statisticsData

        item_analyze_success.valueText = stats.analyzeSuccess.count.toString() + "  (" + BigDecimalFormatter.getCommonFormat().format(stats.analyzeSuccess.percentage) + "%)"
        item_analyze_failed.valueText = stats.analyzeFailed.count.toString() + "  (" + BigDecimalFormatter.getCommonFormat().format(stats.analyzeFailed.percentage) + "%)"
        item_system_apps.valueText = stats.systemApps.count.toString() + "  (" + BigDecimalFormatter.getCommonFormat().format(stats.systemApps.percentage) + "%)"

        chart_min_sdk.columnChartData = data.minSdkChartData
        chart_target_sdk.columnChartData = data.targetSdkChartData
        chart_install_location.columnChartData = data.installLocationChartData
        chart_sign_algorithm.columnChartData = data.signAlgorithChartData
        chart_app_source.columnChartData = data.appSourceChartData

        chart_min_sdk.onValueTouchListener = SdkValueTouchListener(chart_min_sdk, stats.minSdk)
        chart_target_sdk.onValueTouchListener = SdkValueTouchListener(chart_target_sdk, stats.targetSdk)
        chart_install_location.onValueTouchListener = GenericValueTouchListener(chart_install_location, stats.installLocation)
        chart_sign_algorithm.onValueTouchListener = GenericValueTouchListener(chart_sign_algorithm, stats.signAlgorithm)
        chart_app_source.onValueTouchListener = GenericValueTouchListener(chart_app_source, stats.appSource)

        statistics_apk_size.setStatistics(stats.apkSize)
        statistics_activities.setStatistics(stats.activities)
        statistics_services.setStatistics(stats.services)
        statistics_providers.setStatistics(stats.providers)
        statistics_receivers.setStatistics(stats.receivers)
        statistics_used_permissions.setStatistics(stats.usedPermissions)
        statistics_defined_permissions.setStatistics(stats.definedPermissions)
        statistics_files.setStatistics(stats.files)
        statistics_drawables.setStatistics(stats.drawables)
        statistics_drawables_different.setStatistics(stats.differentDrawables)
        statistics_layouts.setStatistics(stats.layouts)
        statistics_layouts_different.setStatistics(stats.differentLayouts)

        local_statistics_content.visibility = View.VISIBLE
        loading_bar.visibility = View.GONE
    }

    override fun onLoaderReset(loader: Loader<LocalStatisticsDataWithCharts>) {
        this.data = null
    }

    override fun onProgressChanged(currentProgress: Int, maxProgress: Int) {
        loading_bar?.setProgress(currentProgress, maxProgress)
    }


    private fun setupChart(chartView: ColumnChartView) {
        chartView.zoomType = ZoomType.HORIZONTAL
        chartView.isScrollEnabled = true
        chartView.setContainerScrollEnabled(true, ContainerScrollType.VERTICAL)
    }

    private inner class SdkValueTouchListener internal constructor(private val chartView: ColumnChartView, private val data: Map<Int, List<String>>) : ColumnChartOnValueSelectListener {

        override fun onValueSelected(columnIndex: Int, subcolumnIndex: Int, value: SubcolumnValue) {
            val label = chartView.columnChartData.axisXBottom.values[columnIndex].labelAsChars
            val labelString = String(label)

            val sdkInteger = Integer.valueOf(labelString)!!
            Toast.makeText(activity, "Selected SDK: " + sdkInteger, Toast.LENGTH_SHORT).show()
            val packageNames = data[sdkInteger] as ArrayList<String>

            AppListDialog.newInstance(packageNames)
                    .show((context as AppCompatActivity).supportFragmentManager, AppListDialog::class.java.simpleName)
        }

        override fun onValueDeselected() {}

    }

    private inner class GenericValueTouchListener internal constructor(private val chartView: ColumnChartView, private val data: Map<*, List<String>>) : ColumnChartOnValueSelectListener {

        override fun onValueSelected(columnIndex: Int, subcolumnIndex: Int, value: SubcolumnValue) {
            val label = chartView.columnChartData.axisXBottom.values[columnIndex].labelAsChars
            val labelString = String(label)

            Toast.makeText(activity, "Selected value: " + labelString, Toast.LENGTH_SHORT).show()
            var packageNames: ArrayList<String>? = null

            for ((key, value1) in data) {
                if (labelString == key.toString()) {
                    packageNames = value1 as ArrayList<String>
                    break
                }
            }

            AppListDialog.newInstance(packageNames!!)
                    .show((context as AppCompatActivity).supportFragmentManager, AppListDialog::class.java.simpleName)
        }

        override fun onValueDeselected() {}

    }

}
