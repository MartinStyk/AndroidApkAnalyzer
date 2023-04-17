package sk.styk.martin.apkanalyzer.ui.manifest

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ManifestRequest(
    val appName: String,
    val packageName: String,
    val apkPath: String,
    val versionName: String?,
    val versionCode: Long,
) : Parcelable
