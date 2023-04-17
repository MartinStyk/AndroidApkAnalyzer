package sk.styk.martin.apkanalyzer.model.detail

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class FileData(
    val dexHash: String = "",
    val arscHash: String = "",
    val manifestHash: String = "",
    val drawableHashes: List<FileEntry> = emptyList(),
    val layoutHashes: List<FileEntry> = emptyList(),
    val menuHashes: List<FileEntry> = emptyList(),
    val otherHashes: List<FileEntry> = emptyList(),
    val numberPngs: Int = 0,
    val numberXmls: Int = 0,
    val numberPngsWithDifferentName: Int = 0,
    val numberXmlsWithDifferentName: Int = 0,
) : Parcelable {

    val drawableByExtension: List<FileEntry>
        get() {
            val imageRegex = Regex(".*(png|gif|jpg|bmp|webp)$")
            val images = ArrayList<FileEntry>()
            drawableHashes.filter { imageRegex.containsMatchIn(it.path) }.mapTo(images) { FileEntry(it.fileName, it.hash) }
            otherHashes.filter { imageRegex.containsMatchIn(it.path) }.mapTo(images) { FileEntry(it.fileName, it.hash) }
            return images
        }

    val totalFiles: Int
        get() {
            return 3 + otherHashes.size + drawableHashes.size + layoutHashes.size + menuHashes.size
        }
}
