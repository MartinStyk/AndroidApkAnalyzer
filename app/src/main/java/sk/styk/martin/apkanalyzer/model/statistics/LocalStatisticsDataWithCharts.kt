package sk.styk.martin.apkanalyzer.model.statistics

import sk.styk.martin.apkanalyzer.util.StatisticsChartDataHelper

class LocalStatisticsDataWithCharts(
        val statisticsData: LocalStatisticsData,
        val minSdkChartData: StatisticsChartDataHelper.BarDataHolder,
        val targetSdkChartData: StatisticsChartDataHelper.BarDataHolder,
        val installLocationChartData: StatisticsChartDataHelper.BarDataHolder,
        val appSourceChartData: StatisticsChartDataHelper.BarDataHolder,
        val signAlgorithChartData: StatisticsChartDataHelper.BarDataHolder
)
