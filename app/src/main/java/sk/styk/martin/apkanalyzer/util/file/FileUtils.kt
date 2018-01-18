package sk.styk.martin.apkanalyzer.util.file

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

    @Throws(IOException::class)
    fun copy(src: File, dst: File) {

        FileInputStream(src).use { input ->
            FileOutputStream(dst).use { output ->

                val buffer = ByteArray(1024)
                var len: Int = input.read(buffer)
                while (len > 0) {
                    len = input.read(buffer)
                    output.write(buffer, 0, len)
                }
            }
        }
    }

    @Throws(IOException::class)
    fun writeString(content: String, targetFilePath: String) {

        PrintWriter(targetFilePath).use {
            it.print(content)
        }

    }
}
