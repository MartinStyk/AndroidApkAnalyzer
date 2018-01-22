package sk.styk.martin.apkanalyzer.util

import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import lecho.lib.hellocharts.model.Axis
import lecho.lib.hellocharts.model.AxisValue
import lecho.lib.hellocharts.model.Column
import lecho.lib.hellocharts.model.ColumnChartData
import lecho.lib.hellocharts.model.SubcolumnValue
import sk.styk.martin.apkanalyzer.ApkAnalyzer
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.model.statistics.LocalStatisticsData
import sk.styk.martin.apkanalyzer.model.statistics.LocalStatisticsDataWithCharts
import java.util.*

/**
 * @author Martin Styk
 * @version 22.01.2018
 */
object ChartDataHelper {

    fun wrapperAround(statisticsData: LocalStatisticsData): LocalStatisticsDataWithCharts =
            LocalStatisticsDataWithCharts(
                    statisticsData = statisticsData,
                    minSdkChartData = getSdkColumnChart(statisticsData.minSdk),
                    targetSdkChartData = getSdkColumnChart(statisticsData.targetSdk),
                    installLocationChartData = getColumnChart(statisticsData.installLocation, R.string.install_loc),
                    appSourceChartData = getColumnChart(statisticsData.appSource, R.string.app_source),
                    signAlgorithChartData = getColumnChart(statisticsData.signAlgorithm, R.string.sign_algorithm)
            )


    fun getSdkColumnChart(map: Map<Int, List<String>>, @ColorRes columnColor: Int = R.color.primary): ColumnChartData {

        val columns = ArrayList<Column>(map.size)
        val axisValues = ArrayList<AxisValue>(map.size)
        var values: MutableList<SubcolumnValue>

        var axisValue = 0
        for (sdk in 1..AndroidVersionHelper.MAX_SDK_VERSION) {
            if (map[sdk] == null)
                continue

            val applicationCount = map[sdk]?.size ?: 0

            values = ArrayList()
            values.add(SubcolumnValue(applicationCount.toFloat(), ContextCompat.getColor(ApkAnalyzer.context, columnColor)))
            val column = Column(values)
            column.setHasLabels(true)
            columns.add(column)

            axisValues.add(AxisValue(axisValue++.toFloat()).setLabel(sdk.toString()))
        }

        val data = ColumnChartData(columns)
        data.axisXBottom = Axis(axisValues).setName(ApkAnalyzer.context.resources.getString(R.string.sdk))
                .setMaxLabelChars(3)
        return data

    }

    fun getColumnChart(map: Map<*, List<String>>, @StringRes axisName: Int, @ColorRes columnColor: Int = R.color.primary): ColumnChartData {

        val columns = ArrayList<Column>(map.size)
        val axisValues = ArrayList<AxisValue>(map.size)
        var values: MutableList<SubcolumnValue>

        var axisValue = 0
        for ((key, value) in map) {

            val applicationCount = value.size

            values = ArrayList()
            values.add(SubcolumnValue(applicationCount.toFloat(), ContextCompat.getColor(ApkAnalyzer.context, columnColor)))
            val column = Column(values)
            column.setHasLabels(true)
            columns.add(column)

            axisValues.add(AxisValue(axisValue++.toFloat()).setLabel(key.toString()))
        }

        val data = ColumnChartData(columns)
        data.axisXBottom = Axis(axisValues).setName(ApkAnalyzer.context.getString(axisName))
                .setMaxLabelChars(10)
        return data
    }


}