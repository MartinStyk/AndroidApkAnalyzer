package sk.styk.martin.apkanalyzer.activity

import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.annotation.StringRes
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
import lecho.lib.hellocharts.model.Axis
import lecho.lib.hellocharts.model.AxisValue
import lecho.lib.hellocharts.model.Column
import lecho.lib.hellocharts.model.ColumnChartData
import lecho.lib.hellocharts.model.SubcolumnValue
import lecho.lib.hellocharts.view.ColumnChartView
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.activity.dialog.AppListDialog
import sk.styk.martin.apkanalyzer.business.task.LocalStatisticsLoader
import sk.styk.martin.apkanalyzer.model.statistics.LocalStatisticsData
import sk.styk.martin.apkanalyzer.util.AndroidVersionHelper
import sk.styk.martin.apkanalyzer.util.BigDecimalFormatter
import java.util.*

/**
 * @author Martin Styk
 */
class LocalStatisticsFragment : Fragment(), LoaderManager.LoaderCallbacks<LocalStatisticsData>, LocalStatisticsLoader.ProgressCallback {

    private var data: LocalStatisticsData? = null

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

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<LocalStatisticsData> {
        return LocalStatisticsLoader(context, this)
    }

    override fun onLoadFinished(loader: Loader<LocalStatisticsData>, data: LocalStatisticsData) {
        this.data = data

        item_analyze_success.valueText = data.analyzeSuccess.count.toString() + "  (" + BigDecimalFormatter.getCommonFormat().format(data.analyzeSuccess.percentage) + "%)"
        item_analyze_failed.valueText = data.analyzeFailed.count.toString() + "  (" + BigDecimalFormatter.getCommonFormat().format(data.analyzeFailed.percentage) + "%)"
        item_system_apps.valueText = data.systemApps.count.toString() + "  (" + BigDecimalFormatter.getCommonFormat().format(data.systemApps.percentage) + "%)"

        chart_min_sdk.columnChartData = getSdkColumnChart(data.minSdk, resources.getColor(R.color.primary))
        chart_target_sdk.columnChartData = getSdkColumnChart(data.targetSdk, resources.getColor(R.color.primary))
        chart_install_location.columnChartData = getColumnChart(data.installLocation, R.string.install_loc, resources.getColor(R.color.primary))
        chart_sign_algorithm.columnChartData = getColumnChart(data.signAlgorithm, R.string.sign_algorithm, resources.getColor(R.color.primary))
        chart_app_source.columnChartData = getColumnChart(data.appSource, R.string.app_source, resources.getColor(R.color.primary))

        chart_min_sdk.onValueTouchListener = SdkValueTouchListener(chart_min_sdk, data.minSdk)
        chart_target_sdk.onValueTouchListener = SdkValueTouchListener(chart_target_sdk, data.targetSdk)
        chart_install_location.onValueTouchListener = GenericValueTouchListener(chart_install_location, data.installLocation)
        chart_sign_algorithm.onValueTouchListener = GenericValueTouchListener(chart_sign_algorithm, data.signAlgorithm)
        chart_app_source.onValueTouchListener = GenericValueTouchListener(chart_app_source, data.appSource)

        statistics_apk_size.setStatistics(data.apkSize)
        statistics_activities.setStatistics(data.activites)
        statistics_services.setStatistics(data.services)
        statistics_providers.setStatistics(data.providers)
        statistics_receivers.setStatistics(data.receivers)
        statistics_used_permissions.setStatistics(data.usedPermissions)
        statistics_defined_permissions.setStatistics(data.definedPermissions)
        statistics_files.setStatistics(data.files)
        statistics_drawables.setStatistics(data.drawables)
        statistics_drawables_different.setStatistics(data.differentDrawables)
        statistics_layouts.setStatistics(data.layouts)
        statistics_layouts_different.setStatistics(data.differentLayouts)

        local_statistics_content.visibility = View.VISIBLE
        loading_bar.visibility = View.GONE
    }

    override fun onLoaderReset(loader: Loader<LocalStatisticsData>) {
        this.data = null
    }

    override fun onProgressChanged(currentProgress: Int, maxProgress: Int) {
        loading_bar?.setProgress(currentProgress, maxProgress)
    }

    private fun getSdkColumnChart(map: Map<Int, List<String>>, @ColorInt columnColor: Int): ColumnChartData {

        val columns = ArrayList<Column>(map.size)
        val axisValues = ArrayList<AxisValue>(map.size)
        var values: MutableList<SubcolumnValue>

        var axisValue = 0
        for (sdk in 1..AndroidVersionHelper.MAX_SDK_VERSION) {
            if (map[sdk] == null)
                continue

            val applicationCount = map[sdk]?.size ?: 0

            values = ArrayList()
            values.add(SubcolumnValue(applicationCount.toFloat(), columnColor))
            val column = Column(values)
            column.setHasLabels(true)
            columns.add(column)

            axisValues.add(AxisValue(axisValue++.toFloat()).setLabel(sdk.toString()))
        }

        val data = ColumnChartData(columns)
        data.axisXBottom = Axis(axisValues).setName(resources.getString(R.string.sdk))
                .setMaxLabelChars(3)
        return data

    }

    private fun getColumnChart(map: Map<*, List<String>>, @StringRes axisName: Int, @ColorInt columnColor: Int): ColumnChartData {

        val columns = ArrayList<Column>(map.size)
        val axisValues = ArrayList<AxisValue>(map.size)
        var values: MutableList<SubcolumnValue>

        var axisValue = 0
        for ((key, value) in map) {

            val applicationCount = value.size

            values = ArrayList()
            values.add(SubcolumnValue(applicationCount.toFloat(), columnColor))
            val column = Column(values)
            column.setHasLabels(true)
            columns.add(column)

            axisValues.add(AxisValue(axisValue++.toFloat()).setLabel(key.toString()))
        }

        val data = ColumnChartData(columns)
        data.axisXBottom = Axis(axisValues).setName(resources.getString(axisName))
                .setMaxLabelChars(10)
        return data

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
