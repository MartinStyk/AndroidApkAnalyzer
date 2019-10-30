package sk.styk.martin.apkanalyzer.util.file

import android.content.Context
import android.content.Intent
import android.net.Uri

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
