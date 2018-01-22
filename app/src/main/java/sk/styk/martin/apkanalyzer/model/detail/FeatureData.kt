package sk.styk.martin.apkanalyzer.model.detail

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author Martin Styk
 * @version 12.10.2017.
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class FeatureData(val name: String,
                       val isRequired: Boolean = false) : Parcelable
