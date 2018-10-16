package sk.styk.martin.apkanalyzer.util.file

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.WorkerThread
import sk.styk.martin.apkanalyzer.R.mipmap.ic_launcher
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.graphics.Canvas
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.provider.MediaStore.Images.Media.getBitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import java.io.*


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
