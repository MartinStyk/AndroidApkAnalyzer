package sk.styk.martin.apkanalyzer.model.detail

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author Martin Styk
 * @version 22.06.2017.
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class BroadcastReceiverData(
        val name: String,
        val permission: String? = null, //Name of permission required to access this receiver
        val isExported: Boolean = false //May be called by another activity
) : Parcelable

