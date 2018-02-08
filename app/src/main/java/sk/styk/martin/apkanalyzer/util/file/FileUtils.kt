package sk.styk.martin.apkanalyzer.util.file

import android.support.annotation.WorkerThread
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintWriter

/**
 * @author Martin Styk
 * @version 03.01.2018.
 */
object FileUtils {

    @WorkerThread
    @Throws(IOException::class)
    fun copy(src: File, dst: File) {

        FileInputStream(src).use { input ->
            FileOutputStream(dst).use { output ->

                val buffer = ByteArray(1024)
                var len: Int = input.read(buffer)
                while (len > 0) {
                    output.write(buffer, 0, len)
                    len = input.read(buffer)
                }
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
}
