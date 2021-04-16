package sk.styk.martin.apkanalyzer.ui.activity.localstatistics

import androidx.databinding.BindingAdapter
import sk.styk.martin.apkanalyzer.util.StatisticsChartDataHelper
import sk.styk.martin.apkanalyzer.views.MyBarChart

object LocalStatisticsFragmentBinding {
    @JvmStatic
    @BindingAdapter("chart_data")
    fun bindChartData(chart: MyBarChart, dataHolder: StatisticsChartDataHelper.BarDataHolder?) =
            dataHolder?.let {
                chart.data = it.data
                chart.xAxis.valueFormatter = it.valueFormatter
            }
}