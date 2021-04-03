package sk.styk.martin.apkanalyzer.util.file

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.annotation.WorkerThread
import java.io.*

object FileUtils {

    val externalRoot by lazy { Environment.getExternalStorageDirectory() }
    val externalAppsDirectory by lazy { File(externalRoot, "ApkAnalyzer/") }

    fun toRelativePath(absolutePath: String) = absolutePath.substring(externalRoot.absolutePath.length + 1)

    interface CopyProgress {
        fun onProgressChanged(progress: Int)
    }

    @WorkerThread
    @Throws(IOException::class)
    fun copy(src: File, dst: File, callback: CopyProgress? = null) {

        FileInputStream(src).use {
            copy(it, dst, callback, src.length())
        }

    }

    @WorkerThread
    @Throws(IOException::class)
    fun copy(src: InputStream, dst: File, callback: CopyProgress? = null, fileSize: Long = 1) {

        FileOutputStream(dst).use { output ->

            val buffer = ByteArray(10240)
            var len: Int = src.read(buffer)
            var readBytes = len
            while (len > 0) {
                output.write(buffer, 0, len)
                len = src.read(buffer)
                readBytes += len
                callback?.onProgressChanged((readBytes * 100 / fileSize).toInt())
            }
        }
    }

    @WorkerThread
    @Throws(IOException::class)
    fun writeString(content: String, targetFilePath: String) {

        PrintWriter(targetFilePath).use {
            it.print(content)
        }

    }

    @WorkerThread
    fun uriToPatch(uri: Uri?, context: Context): String? {
        return if (uri == null) null else fromUri(uri, context)?.absolutePath
    }

    @WorkerThread
    fun fromUri(uri: Uri, context: Context): File? {

        return try {
            val tempFile = File.createTempFile("analysed", ".apk")
            tempFile.deleteOnExit()

            context.contentResolver.openInputStream(uri).use { inputStream ->

                if (inputStream != null) {
                    copy(inputStream, tempFile)
                }
                tempFile
            }
        } catch (exception: Exception) {
            null
        }
    }
}
