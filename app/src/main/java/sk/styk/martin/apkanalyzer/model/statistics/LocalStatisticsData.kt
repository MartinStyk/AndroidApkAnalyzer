package sk.styk.martin.apkanalyzer.model.statistics

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import sk.styk.martin.apkanalyzer.model.detail.AppSource
import sk.styk.martin.apkanalyzer.util.PercentagePair

@SuppressLint("ParcelCreator")
@Parcelize
data class LocalStatisticsData(
        val analyzeSuccess: PercentagePair,
        val analyzeFailed: PercentagePair,
        val systemApps: PercentagePair,
        val installLocation: Map<String, List<String>>,
        val targetSdk: Map<Int, List<String>>,
        val minSdk: Map<Int, List<String>>,
        val appSource: Map<AppSource, List<String>>,
        val apkSize: MathStatistics,
        val signAlgorithm: Map<String, List<String>>,
        val activities: MathStatistics,
        val services: MathStatistics,
        val providers: MathStatistics,
        val receivers: MathStatistics,
        val usedPermissions: MathStatistics,
        val definedPermissions: MathStatistics,
        val files: MathStatistics,
        val drawables: MathStatistics,
        val differentDrawables: MathStatistics,
        val layouts: MathStatistics,
        val differentLayouts: MathStatistics
) : Parcelable
