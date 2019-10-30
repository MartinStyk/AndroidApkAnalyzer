package sk.styk.martin.apkanalyzer.model.statistics

import sk.styk.martin.apkanalyzer.util.ChartDataHelper

class LocalStatisticsDataWithCharts(
        val statisticsData: LocalStatisticsData,
        val minSdkChartData: ChartDataHelper.BarDataHolder,
        val targetSdkChartData: ChartDataHelper.BarDataHolder,
        val installLocationChartData: ChartDataHelper.BarDataHolder,
        val appSourceChartData: ChartDataHelper.BarDataHolder,
        val signAlgorithChartData: ChartDataHelper.BarDataHolder
)
