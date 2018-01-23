package sk.styk.martin.apkanalyzer.model.detail

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Single used permission entry
 *
 * @author Martin Styk
 * @version 13.01.2018.
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class UsedPermissionData(
        val permissionData: PermissionData,
        var isGranted: Boolean) : Parcelable
