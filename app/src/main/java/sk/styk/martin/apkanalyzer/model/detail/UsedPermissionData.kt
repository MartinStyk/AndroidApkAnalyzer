package sk.styk.martin.apkanalyzer.model.detail

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class UsedPermissionData(
    val permissionData: PermissionData,
    var isGranted: Boolean,
) : Parcelable
