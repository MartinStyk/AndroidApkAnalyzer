package sk.styk.martin.apkanalyzer.core.appanalysis.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class BroadcastReceiverData(
    val name: String,
    val permission: String? = null, // Name of permission required to access this receiver
    val isExported: Boolean = false, // May be called by another activity
) : Parcelable
