package sk.styk.martin.apkanalyzer.util.bindingadapters

import androidx.databinding.BindingAdapter
import sk.styk.martin.apkanalyzer.util.StatisticsChartDataHelper
import sk.styk.martin.apkanalyzer.views.MyBarChart

@BindingAdapter("chart_data")
fun MyBarChart.bindChartData(dataHolder: StatisticsChartDataHelper.BarDataHolder?) =
        dataHolder?.let {
            data = it.data
            xAxis.valueFormatter = it.valueFormatter
        }