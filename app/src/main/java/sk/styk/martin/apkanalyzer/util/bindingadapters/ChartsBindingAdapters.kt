package sk.styk.martin.apkanalyzer.util.bindingadapters

import androidx.databinding.BindingAdapter
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import sk.styk.martin.apkanalyzer.views.chart.MyBarChart

@BindingAdapter("xAxis_valueFormatter")
fun MyBarChart.xAxisValueFormatter(formatter: IAxisValueFormatter?) {
    xAxis.valueFormatter = formatter
}
