package sk.styk.martin.apkanalyzer.core.applist.model

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import kotlinx.parcelize.IgnoredOnParcel
import sk.styk.martin.apkanalyzer.core.appanalysis.model.AppSource
import timber.log.Timber

data class LazyAppListData(
    val packageName: String,
    val isSystemApp: Boolean,
    val version: Int,
    val source: AppSource,
)  {

    private var packageManager: PackageManager? = null

    private var applicationInfo: ApplicationInfo? = null

    val applicationName: String by lazy {
        packageManager?.let { applicationInfo?.loadLabel(it) }?.toString() ?: packageName
    }

    val icon: Drawable? by lazy {
        kotlin.runCatching { applicationInfo?.loadIcon(packageManager) }
            .onFailure { Timber.e(it, "Icon not available for %s", packageName) }
            .getOrNull()
    }

    constructor(
        packageName: String,
        isSystemApp: Boolean,
        version: Int,
        source: AppSource,
        packageManager: PackageManager,
        packageInfo: PackageInfo,
    ) : this(
        packageName, isSystemApp, version, source
    ) {
        this.packageManager = packageManager
        this.applicationInfo = packageInfo.applicationInfo
    }

}