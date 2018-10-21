package sk.styk.martin.apkanalyzer.model.statistics

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import sk.styk.martin.apkanalyzer.model.detail.AppSource

/**
 * Represents single app data for statistics computation.
 *
 * @author Martin Styk
 * @version 28.07.2017.
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class LocalStatisticsAppData(
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
        var files: Int = 0,
        var drawables: Int = 0,
        var differentDrawables: Int = 0,
        var layouts: Int = 0,
        var differentLayouts: Int = 0) : Parcelable
