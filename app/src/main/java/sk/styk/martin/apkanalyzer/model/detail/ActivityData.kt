package sk.styk.martin.apkanalyzer.model.detail

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class ActivityData(
        val name: String,
        val packageName: String? = null,
        val label: String? = null,
        val targetActivity: String? = null, // If this is an activity alias, this is the real activity class to run for it.
        val permission: String? = null, // Optional name of a permission required to be able to access this Ativity
        val parentName: String? = null, // If defined, the activity named here is the logical parent of this activity
        val isExported: Boolean = false // Set to true if this component is available for use by other applications.
) : Parcelable

