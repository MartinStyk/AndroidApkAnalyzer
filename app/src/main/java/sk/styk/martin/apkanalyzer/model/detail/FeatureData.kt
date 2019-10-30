package sk.styk.martin.apkanalyzer.model.detail

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class FeatureData(val name: String,
                       val isRequired: Boolean = false) : Parcelable
