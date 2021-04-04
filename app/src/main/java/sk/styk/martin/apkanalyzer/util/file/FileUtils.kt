package sk.styk.martin.apkanalyzer.util.file

import android.os.Environment
import androidx.annotation.WorkerThread
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield
import java.io.*

object FileUtils {

    val externalRoot by lazy { Environment.getExternalStorageDirectory() }
    val externalAppsDirectory by lazy { File(externalRoot, "ApkAnalyzer/") }

    fun toRelativePath(absolutePath: String) = absolutePath.substring(externalRoot.absolutePath.length + 1)

    @WorkerThread
    @Throws(IOException::class)
    suspend fun copy(src: InputStream, dst: File) {

        FileOutputStream(dst).use { output ->

            val buffer = ByteArray(10240)
            var len: Int = src.read(buffer)
            var readBytes = len
            while (len > 0) {
                yield()
                output.write(buffer, 0, len)
                len = src.read(buffer)
                readBytes += len
            }
        }
        delay(500)
    }

    @WorkerThread
    @Throws(IOException::class)
    fun writeString(content: String, targetFilePath: String) {

        PrintWriter(targetFilePath).use {
            it.print(content)
        }

    }


}
