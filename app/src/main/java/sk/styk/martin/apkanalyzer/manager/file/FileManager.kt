package sk.styk.martin.apkanalyzer.manager.file

import android.content.Context
import android.net.Uri
import android.os.Environment
import kotlinx.coroutines.withContext
import sk.styk.martin.apkanalyzer.dependencyinjection.util.ForApplication
import sk.styk.martin.apkanalyzer.util.coroutines.DispatcherProvider
import sk.styk.martin.apkanalyzer.util.file.FileUtils
import java.io.File
import java.io.IOException
import javax.inject.Inject

class FileManager @Inject constructor(@ForApplication private val context: Context,
                                      private val dispatcherProvider: DispatcherProvider) {

    val externalDirectory by lazy {
        context.getExternalFilesDir(null) ?: throw IOException("External directory not available")
    }

    val dcimDirectory by lazy {
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                ?: throw IOException("Pictures directory not available")
    }

    val cacheDirectory by lazy { context.cacheDir }

    @Throws(IOException::class)
    suspend fun createTempFileFromUri(uri: Uri, fileName: String): File = withContext(dispatcherProvider.io()) {
        val tempFile = File(cacheDirectory, fileName)
        context.contentResolver.openInputStream(uri).use { inputStream ->
            if (inputStream != null) {
                FileUtils.copy(inputStream, tempFile)
            }
            tempFile
        }
    }

    @Throws(IOException::class)
    fun deleteTempFile(fileName: String)  {
        val tempFile = File(cacheDirectory, fileName)
        tempFile.delete()
    }

}