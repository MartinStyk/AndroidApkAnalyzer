package sk.styk.martin.apkanalyzer.model.detail

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class ContentProviderData(
        val name: String,
        val authority: String, //the name provider is published under this content
        val readPermission: String? = null, //Optional permission required for read-only access this content provider
        val writePermission: String? = null, //Optional permission required for read/write access this content provider
        val isExported: Boolean = false //May be called by another activity
) : Parcelable

