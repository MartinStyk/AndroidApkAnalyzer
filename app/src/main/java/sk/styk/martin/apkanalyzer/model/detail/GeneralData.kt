package sk.styk.martin.apkanalyzer.model.detail

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

/**
 * @author Martin Styk
 * @version 01.07.2017.
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class GeneralData(
        val packageName: String,
        val applicationName: String,
        val processName: String? = null,
        val versionName: String? = null,
        val versionCode: Int = 0,
        val isSystemApp: Boolean = false,
        val uid: Int? = null,
        val description: String? = null,
        val source: AppSource = AppSource.UNKNOWN,
        val apkDirectory: String,
        val dataDirectory: String? = null,
        val installLocation: String,
        val appInstaller: String? = null, // TODO add to view

        val apkSize: Long = 0, //bytes

        val firstInstallTime: Long? = null, //timestamp
        val lastUpdateTime: Long? = null, //timestamp

        val minSdkVersion: Int? = null,
        val minSdkLabel: String? = null,

        val targetSdkVersion: Int? = null,
        val targetSdkLabel: String? = null
) : Parcelable {

    @IgnoredOnParcel
    var icon: Drawable? = null
        private set

    constructor(packageName: String,
                applicationName: String,
                processName: String?,
                versionName: String?,
                versionCode: Int,
                isSystemApp: Boolean,
                uid: Int,
                description: String?,
                source: AppSource,
                apkDirectory: String,
                dataDirectory: String?,
                installLocation: String,
                appInstaller: String?,
                apkSize: Long = 0,
                firstInstallTime: Long?,
                lastUpdateTime: Long?,
                minSdkVersion: Int?,
                minSdkLabel: String?,
                targetSdkVersion: Int,
                targetSdkLabel: String?,
                icon: Drawable?) : this(
            packageName,
            applicationName,
            processName,
            versionName,
            versionCode,
            isSystemApp,
            uid,
            description,
            source,
            apkDirectory,
            dataDirectory,
            installLocation,
            appInstaller,
            apkSize,
            firstInstallTime,
            lastUpdateTime,
            minSdkVersion,
            minSdkLabel,
            targetSdkVersion,
            targetSdkLabel) {
        this.icon = icon
    }

}
