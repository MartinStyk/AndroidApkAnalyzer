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
data class FileEntry(val path: String, val hash: String) : Parcelable {

    val fileName: String
        get() {
            val lastSlash = path.lastIndexOf("/")
            return if (lastSlash < 0) path else path.substring(lastSlash)
        }
}