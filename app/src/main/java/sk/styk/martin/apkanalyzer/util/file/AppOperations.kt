package sk.styk.martin.apkanalyzer.util.file

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import sk.styk.martin.apkanalyzer.BuildConfig
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.util.TAG_APP_ACTIONS
import timber.log.Timber

object AppOperations {

    fun openAppSystemPage(context: Context, packageName: String) {
        try {
            context.startActivity(
                Intent().apply {
                    action = android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.parse("package:$packageName")
                },
            )
        } catch (exception: Exception) {
            Timber.tag(TAG_APP_ACTIONS).e(exception, "Could not open system page for $packageName.")
        }
    }

    @JvmStatic
    fun installPremium(context: Context) {
        openGooglePlay(context, BuildConfig.APPLICATION_ID + ".premium")
    }

    @JvmStatic
    fun openGooglePlay(context: Context, packageName: String) {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
        } catch (activityNotFound: ActivityNotFoundException) {
            // Google Play not installed, open it in browser
            Timber.tag(TAG_APP_ACTIONS).w(activityNotFound, "Starting Google play failed. Try to open it in browser.")
            openBrowser(context, "https://play.google.com/store/apps/details?id=$packageName")
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

    @JvmStatic
    fun openBrowser(context: Context, url: String) {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } catch (activityNotFoundException: ActivityNotFoundException) {
            Timber.tag(TAG_APP_ACTIONS).w(activityNotFoundException, "Can not open browser.")
            Toast.makeText(context, R.string.activity_run_failed, Toast.LENGTH_SHORT).show()
        }
    }
}
