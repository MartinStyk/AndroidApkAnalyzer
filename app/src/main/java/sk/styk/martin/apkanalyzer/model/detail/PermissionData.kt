package sk.styk.martin.apkanalyzer.model.detail

import android.annotation.SuppressLint
import android.content.pm.PermissionInfo
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import sk.styk.martin.apkanalyzer.manager.appanalysis.AppPermissionManager

@SuppressLint("ParcelCreator")
@Parcelize
data class PermissionData(val name: String,
                          val simpleName: String = AppPermissionManager.createSimpleName(name),
                          val groupName: String? = null,
                          val protectionLevel: Int = PermissionInfo.PROTECTION_NORMAL) : Parcelable