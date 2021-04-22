package sk.styk.martin.apkanalyzer.util.file

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import sk.styk.martin.apkanalyzer.BuildConfig
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.util.TAG_APP_ACTIONS
import timber.log.Timber
import java.io.File

object AppOperations {

    fun installPackage(context: Context, packagePath: String) {
        val intent = Intent(Intent.ACTION_VIEW)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                .addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                .addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

        try {
            val apkUri = FileProvider.getUriForFile(context, GenericFileProvider.AUTHORITY, File(packagePath))
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
            context.startActivity(intent)
        } catch (e: Exception) {
            Timber.tag(TAG_APP_ACTIONS).e(e, "Could not install app from path $packagePath.")
            Toast.makeText(context, context.getString(R.string.install_failed), Toast.LENGTH_LONG).show()
        }
    }

    fun openAppSystemPage(context: Context, packageName: String) {
        val systemInfoIntent = Intent()
        systemInfoIntent.action = android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        systemInfoIntent.data = Uri.parse("package:" + packageName)

        context.startActivity(systemInfoIntent)
    }

    @JvmStatic
    fun installPremium(context: Context) {
        openGooglePlay(context, BuildConfig.APPLICATION_ID + ".premium")
    }

    @JvmStatic
    fun openGooglePlay(context: Context, packageName: String) {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)))
        } catch (anfe: ActivityNotFoundException) {
            // Google Play not installed, open it in browser
            Timber.tag(TAG_APP_ACTIONS).w(anfe, "Starting Google play failed. Try to open it in browser.")
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)))
        }

    }

    @JvmStatic
    fun startForeignActivity(context: Context, packageName: String, activityName: String) {
        val intent = Intent().apply {
            component = ComponentName(packageName, activityName)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Timber.tag(TAG_APP_ACTIONS).e(e, "Starting foreign activity failed. Intent was $intent")
            Toast.makeText(context, R.string.activity_run_failed, Toast.LENGTH_SHORT).show()
        }
    }

}
