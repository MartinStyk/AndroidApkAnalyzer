package sk.styk.martin.apkanalyzer.model.statistics

import lecho.lib.hellocharts.model.ColumnChartData

/**
 * @author Martin Styk
 * @version 28.07.2017.
 */
class LocalStatisticsDataWithCharts(
        val statisticsData: LocalStatisticsData,
        val minSdkChartData: ColumnChartData,
        val targetSdkChartData: ColumnChartData,
        val installLocationChartData: ColumnChartData,
        val appSourceChartData: ColumnChartData,
        val signAlgorithChartData: ColumnChartData
)
