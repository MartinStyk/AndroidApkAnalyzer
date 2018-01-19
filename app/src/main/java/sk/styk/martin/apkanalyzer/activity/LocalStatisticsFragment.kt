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

import java.util.ArrayList

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
import sk.styk.martin.apkanalyzer.databinding.FragmentLocalStatisticsBinding
import sk.styk.martin.apkanalyzer.model.statistics.LocalStatisticsData
import sk.styk.martin.apkanalyzer.util.AndroidVersionHelper
import sk.styk.martin.apkanalyzer.util.BigDecimalFormatter

/**
 * @author Martin Styk
 */
class LocalStatisticsFragment : Fragment(), LoaderManager.LoaderCallbacks<LocalStatisticsData>, LocalStatisticsLoader.ProgressCallback {

    internal var binding: FragmentLocalStatisticsBinding
    internal var data: LocalStatisticsData? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentLocalStatisticsBinding.inflate(inflater)

        // We need to re-set callback of loader in case of configuration change
        val loader = loaderManager.initLoader(LocalStatisticsLoader.ID, null, this) as LocalStatisticsLoader
        loader?.setCallbackReference(this)

        setHasOptionsMenu(true)

        setupChart(binding.chartMinSdk)
        setupChart(binding.chartTargetSdk)
        setupChart(binding.chartInstallLocation)
        setupChart(binding.chartSignAlgorithm)
        setupChart(binding.chartAppSource)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        // Hide action bar item for searching
        menu!!.clear()

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateLoader(id: Int, args: Bundle): Loader<LocalStatisticsData> {
        return LocalStatisticsLoader(context, this)
    }

    override fun onLoadFinished(loader: Loader<LocalStatisticsData>, data: LocalStatisticsData) {
        this.data = data

        binding.itemAnalyzeSuccess.valueText = data.analyzeSuccess.count.toString() + "  (" + BigDecimalFormatter.getCommonFormat().format(data.analyzeSuccess.percentage) + "%)"
        binding.itemAnalyzeFailed.valueText = data.analyzeFailed.count.toString() + "  (" + BigDecimalFormatter.getCommonFormat().format(data.analyzeFailed.percentage) + "%)"
        binding.itemSystemApps.valueText = data.systemApps.count.toString() + "  (" + BigDecimalFormatter.getCommonFormat().format(data.systemApps.percentage) + "%)"

        binding.chartMinSdk.columnChartData = getSdkColumnChart(data.minSdk, resources.getColor(R.color.primary))
        binding.chartTargetSdk.columnChartData = getSdkColumnChart(data.targetSdk, resources.getColor(R.color.primary))
        binding.chartInstallLocation.columnChartData = getColumnChart(data.installLocation, R.string.install_loc, resources.getColor(R.color.primary))
        binding.chartSignAlgorithm.columnChartData = getColumnChart(data.signAlgorithm, R.string.sign_algorithm, resources.getColor(R.color.primary))
        binding.chartAppSource.columnChartData = getColumnChart(data.appSource, R.string.app_source, resources.getColor(R.color.primary))

        binding.chartMinSdk.onValueTouchListener = SdkValueTouchListener(binding.chartMinSdk, data.minSdk)
        binding.chartTargetSdk.onValueTouchListener = SdkValueTouchListener(binding.chartTargetSdk, data.targetSdk)
        binding.chartInstallLocation.onValueTouchListener = GenericValueTouchListener(binding.chartInstallLocation, data.installLocation)
        binding.chartSignAlgorithm.onValueTouchListener = GenericValueTouchListener(binding.chartSignAlgorithm, data.signAlgorithm)
        binding.chartAppSource.onValueTouchListener = GenericValueTouchListener(binding.chartAppSource, data.appSource)

        binding.statisticsApkSize.setStatistics(data.apkSize)
        binding.statisticsActivities.setStatistics(data.activites)
        binding.statisticsServices.setStatistics(data.services)
        binding.statisticsProviders.setStatistics(data.providers)
        binding.statisticsReceivers.setStatistics(data.receivers)
        binding.statisticsUsedPermissions.setStatistics(data.usedPermissions)
        binding.statisticsDefinedPermissions.setStatistics(data.definedPermissions)
        binding.statisticsFiles.setStatistics(data.files)
        binding.statisticsDrawables.setStatistics(data.drawables)
        binding.statisticsDrawablesDifferent.setStatistics(data.differentDrawables)
        binding.statisticsLayouts.setStatistics(data.layouts)
        binding.statisticsLayoutsDifferent.setStatistics(data.differentLayouts)

        binding.localStatisticsContent.visibility = View.VISIBLE
        binding.loadingBar.visibility = View.GONE
    }

    override fun onLoaderReset(loader: Loader<LocalStatisticsData>) {
        this.data = null
    }

    override fun onProgressChanged(currentProgress: Int, maxProgress: Int) {
        binding.loadingBar.setProgress(currentProgress, maxProgress)
    }

    private fun getSdkColumnChart(map: Map<Int, List<String>>, @ColorInt columnColor: Int): ColumnChartData {

        val columns = ArrayList<Column>(map.size)
        val axisValues = ArrayList<AxisValue>(map.size)
        var values: MutableList<SubcolumnValue>

        var axisValue = 0
        for (sdk in 1..AndroidVersionHelper.MAX_SDK_VERSION) {
            if (map[sdk] == null)
                continue

            val applicationCount = map[sdk].size

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
