package sk.styk.martin.apkanalyzer.core.appanalysis.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class FeatureData(
    val name: String,
    val isRequired: Boolean = false,
) : Parcelable
