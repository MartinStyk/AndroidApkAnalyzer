package sk.styk.martin.apkanalyzer.ui.activity.localstatistics

import androidx.databinding.BindingAdapter
import sk.styk.martin.apkanalyzer.util.ChartDataHelper
import sk.styk.martin.apkanalyzer.views.MyBarChart

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