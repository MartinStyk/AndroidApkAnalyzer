package sk.styk.martin.apkanalyzer.model.statistics

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import sk.styk.martin.apkanalyzer.model.detail.AppSource

@SuppressLint("ParcelCreator")
@Parcelize
data class StatisticsAppData(
        var packageName: String,
        var isSystemApp: Boolean = false,
        var installLocation: Int = 0,
        var targetSdk: Int = 0,
        var minSdk: Int = 0,
        var apkSize: Long = 0,
        var appSource: AppSource = AppSource.UNKNOWN,
        var signAlgorithm: String,
        var activities: Int = 0,
        var services: Int = 0,
        var providers: Int = 0,
        var receivers: Int = 0,
        var usedPermissions: Int = 0,
        var definedPermissions: Int = 0,
) : Parcelable
