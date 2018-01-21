package sk.styk.martin.apkanalyzer.model.detail

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author Martin Styk
 * @version 06.11.2017.
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class ClassPathData(
        val packageClasses: List<String> = emptyList(),
        val otherClasses: List<String> = emptyList(),
        val numberOfInnerClasses: Int = 0
) : Parcelable
