package sk.styk.martin.apkanalyzer.core.appstatistics.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import sk.styk.martin.apkanalyzer.core.appanalysis.model.AppSource
import sk.styk.martin.apkanalyzer.core.appanalysis.model.InstallLocation

@SuppressLint("ParcelCreator")
@Parcelize
data class StatisticsData(
    val analyzeSuccess: PercentagePair,
    val analyzeFailed: PercentagePair,
    val systemApps: PercentagePair,
    val installLocation: Map<InstallLocation, List<String>>,
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
) : Parcelable
