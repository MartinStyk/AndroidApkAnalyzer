package sk.styk.martin.apkanalyzer.util.file

import android.os.Environment
import androidx.annotation.WorkerThread
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

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
    fun writeString(content: String, targetFile: File) {
        val parent = targetFile.parentFile
        if (parent?.exists() == false) {
            parent.mkdirs()
        }

        targetFile.printWriter().use {
            it.println(content)
        }
    }


}
