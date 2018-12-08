package sk.styk.martin.apkanalyzer.util.file

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.support.annotation.WorkerThread
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.PrintWriter


/**
 * @author Martin Styk
 * @version 03.01.2018.
 */
object FileUtils {

    @WorkerThread
    @Throws(IOException::class)
    fun copy(src: File, dst: File) {

        FileInputStream(src).use {
            copy(it, dst)
        }
    }

    @WorkerThread
    @Throws(IOException::class)
    fun copy(src: InputStream, dst: File) {

        FileOutputStream(dst).use { output ->

            val buffer = ByteArray(1024)
            var len: Int = src.read(buffer)
            while (len > 0) {
                output.write(buffer, 0, len)
                len = src.read(buffer)
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
    @Throws(IOException::class)
    fun writeBitmap(bitmap: Bitmap, targetFilePath: String) {
        val imageFile = File(targetFilePath)

        FileOutputStream(imageFile).use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
    }

    @WorkerThread
    @Throws(IOException::class)
    fun uriToPatch(uri: Uri?, context: Context): String? {
        return if (uri == null) null else fromUri(uri, context)?.absolutePath
    }

    @WorkerThread
    @Throws(IOException::class)
    fun fromUri(uri: Uri, context: Context): File? {

        return try {
            var tempFile = File.createTempFile("analysed", ".apk")
            tempFile.deleteOnExit()

            context.contentResolver.openInputStream(uri).use { inputStream ->

                if (inputStream != null) {
                    copy(inputStream, tempFile)
                }
                tempFile
            }
        } catch (exception: IOException) {
            null
        }
    }
}
