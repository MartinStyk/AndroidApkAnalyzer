package sk.styk.martin.apkanalyzer.util.file

import android.graphics.drawable.Drawable
import android.support.annotation.WorkerThread
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintWriter
import sk.styk.martin.apkanalyzer.R.mipmap.ic_launcher
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.graphics.Canvas
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.provider.MediaStore.Images.Media.getBitmap
import android.graphics.drawable.BitmapDrawable





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

    @WorkerThread
    @Throws(IOException::class)
    fun writeBitmap(bitmap: Bitmap, targetFilePath: String) {
        val imageFile = File(targetFilePath)

        FileOutputStream(imageFile).use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
    }


}
