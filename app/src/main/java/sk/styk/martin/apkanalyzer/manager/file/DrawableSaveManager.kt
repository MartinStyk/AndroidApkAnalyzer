package sk.styk.martin.apkanalyzer.manager.file

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import kotlinx.coroutines.withContext
import sk.styk.martin.apkanalyzer.manager.media.MediaManager
import sk.styk.martin.apkanalyzer.util.coroutines.DispatcherProvider
import sk.styk.martin.apkanalyzer.util.file.toBitmap
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

private const val SUBDIRECTORY = "ApkAnalyzer"

class DrawableSaveManager @Inject constructor(
        private val fileManager: FileManager,
        private val mediaManager: MediaManager,
        private val dispatcherProvider: DispatcherProvider) {

    suspend fun saveDrawable(drawable: Drawable, fileName: String): File = withContext(dispatcherProvider.io()) {
        val bitmap = drawable.toBitmap()
        val imageFile = File(fileManager.dcimDirectory, SUBDIRECTORY + fileName)

        FileOutputStream(imageFile).use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }

        mediaManager.addMediaRecord(imageFile)

        imageFile
    }

}