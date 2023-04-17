package sk.styk.martin.apkanalyzer.model.detail

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class FileEntry(val path: String, val hash: String) : Parcelable {

    val fileName: String
        get() {
            val lastSlash = path.lastIndexOf("/")
            return if (lastSlash < 0 || lastSlash >= path.length) path else path.substring(lastSlash + 1)
        }
}
