package sk.styk.martin.apkanalyzer.model.detail

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author Martin Styk
 * @version 02.07.2017.
 */
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
        val numberXmlsWithDifferentName: Int = 0
) : Parcelable {


    val pngHashes: List<FileEntry>
        get() {
            val pngs = ArrayList<FileEntry>()
            drawableHashes.filter { it.path.endsWith(".png") }.mapTo(pngs){ FileEntry(it.fileName, it.hash) }
            otherHashes.filter { it.path.endsWith(".png") }.mapTo(pngs){FileEntry(it.fileName, it.hash)}
            return pngs
        }

    val totalFiles: Int
        get() {
            return 3 + otherHashes.size + drawableHashes.size + layoutHashes.size + menuHashes.size
        }

    // TODO remove this
    fun getOnlyHash(fileEntries: List<FileEntry>): List<String> = fileEntries.mapTo(ArrayList()) { it.hash }

}
