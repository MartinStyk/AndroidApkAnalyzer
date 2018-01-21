package sk.styk.martin.apkanalyzer.model.permissions

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Permission granted/ not granted status for package
 *
 * @author Martin Styk
 * @version 13.01.2018.
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class PermissionStatus(
        val packageName: String,
        val isGranted: Boolean = false
) : Parcelable
