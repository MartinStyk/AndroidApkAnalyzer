package sk.styk.martin.apkanalyzer.util

import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import sk.styk.martin.apkanalyzer.ApkAnalyzer
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.model.statistics.LocalStatisticsData
import sk.styk.martin.apkanalyzer.model.statistics.LocalStatisticsDataWithCharts
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

class StatisticsChartDataHelper @Inject constructor() {

    fun wrapperAround(statisticsData: LocalStatisticsData): LocalStatisticsDataWithCharts =
            LocalStatisticsDataWithCharts(
                    statisticsData = statisticsData,
                    minSdkChartData = getBarSdkData(statisticsData.minSdk),
                    targetSdkChartData = getBarSdkData(statisticsData.targetSdk),
                    installLocationChartData = getBarData(statisticsData.installLocation),
                    appSourceChartData = getBarData(statisticsData.appSource),
                    signAlgorithChartData = getBarData(statisticsData.signAlgorithm)
            )

    private fun getBarSdkData(map: Map<Int, List<String>>,
                              @ColorRes columnColor: Int = R.color.graph_bar,
                              @ColorRes selectedColumnColor: Int = R.color.accent): BarDataHolder {


        val values = ArrayList<BarEntry>(map.size)
        val axisValues = ArrayList<String>(map.size)
        var index = 0f
        for (sdk in 1..AndroidVersionHelper.MAX_SDK_VERSION) {
            if (map[sdk] == null)
                continue

            val applicationCount = map[sdk]?.size ?: 0

            values.add(BarEntry(index++, applicationCount.toFloat(), map[sdk]))
            axisValues.add(sdk.toString())
        }

        return BarDataHolder(
                BarData(listOf<IBarDataSet>(
                        BarDataSet(values, "mLabel")
                                .apply {
                                    color = ContextCompat.getColor(ApkAnalyzer.context, columnColor)
                                    highLightColor = ContextCompat.getColor(ApkAnalyzer.context, selectedColumnColor)
                                    valueTextColor = color
                                })),
                IAxisValueFormatter { i, _ -> axisValues[i.roundToInt()] })
    }

    private fun getBarData(map: Map<*, List<String>>,
                           @ColorRes columnColor: Int = R.color.graph_bar,
                           @ColorRes selectedColumnCOlor: Int = R.color.accent): BarDataHolder {

        val values = mutableListOf<BarEntry>()
        val axisValues = mutableListOf<String>()
        var index = 1f
        for ((key, value) in map) {

            values.add(BarEntry(index++, value.size.toFloat(), value))
            axisValues.add(key.toString())
        }

        return BarDataHolder(
                BarData(listOf<IBarDataSet>(
                        BarDataSet(values, "mLabel")
                                .apply {
                                    color = ContextCompat.getColor(ApkAnalyzer.context, columnColor)
                                    highLightColor = ContextCompat.getColor(ApkAnalyzer.context, selectedColumnCOlor)
                                    valueTextColor = color
                                    isHighlightEnabled = true
                                })),
                IAxisValueFormatter { i, _ -> axisValues[i.roundToInt() - 1] })
    }

    class BarDataHolder(
            val data: BarData,
            val valueFormatter: IAxisValueFormatter)
}