package sk.styk.martin.apkanalyzer.core.appanalysis.model

import android.annotation.SuppressLint
import android.content.pm.PermissionInfo
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import sk.styk.martin.apkanalyzer.core.appanalysis.AppPermissionManager

@SuppressLint("ParcelCreator")
@Parcelize
data class PermissionData(
    val name: String,
    val simpleName: String = AppPermissionManager.createSimpleName(name),
    val groupName: String? = null,
    val protectionLevel: Int = PermissionInfo.PROTECTION_NORMAL,
) : Parcelable
