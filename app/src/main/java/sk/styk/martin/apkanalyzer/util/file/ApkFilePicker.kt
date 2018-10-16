package sk.styk.martin.apkanalyzer.util.file

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import com.google.android.gms.tasks.Task
import sk.styk.martin.apkanalyzer.ApkAnalyzer.Companion.context
import java.io.File

/**
 * @author Martin Styk
 * @version 09.10.2017.
 */

object ApkFilePicker {

    const val REQUEST_PICK_APK = 1324

    val filePickerIntent: Intent
        get() {
            val mediaIntent = Intent(Intent.ACTION_GET_CONTENT)
            mediaIntent.type = "application/vnd.android.package-archive"
            return mediaIntent
        }

    fun getPathFromIntentData(apkUri: Uri?, context: Context): String? {
        apkUri ?: return null

        return FileUtils.fromUri(apkUri, context)?.absolutePath
    }

}
