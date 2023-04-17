package sk.styk.martin.apkanalyzer.model.detail

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class ClassPathData(
    val packageClasses: List<String> = emptyList(),
    val otherClasses: List<String> = emptyList(),
    val numberOfInnerClasses: Int = 0,
) : Parcelable {

    val allClasses: List<String>
        get() {
            val allClasses = ArrayList(packageClasses)
            allClasses.addAll(otherClasses)
            return allClasses
        }
}
