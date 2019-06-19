package sk.styk.martin.apkanalyzer.ui.activity.localstatistics

import androidx.databinding.BindingAdapter
import sk.styk.martin.apkanalyzer.ui.customview.MyBarChart
import sk.styk.martin.apkanalyzer.util.ChartDataHelper

/**
 * @author Martin Styk {@literal <martin.styk@gmail.com>}
 */
object LocalStatisticsFragmentBinding {
    @JvmStatic
    @BindingAdapter("chart_data")
    fun bindChartData(chart: MyBarChart, dataHolder: ChartDataHelper.BarDataHolder?) =
            dataHolder?.let {
                chart.data = it.data
                chart.data.isHighlightEnabled = true
                chart.xAxis.valueFormatter = it.valueFormatter
            }
}