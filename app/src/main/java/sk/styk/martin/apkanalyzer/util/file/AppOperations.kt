package sk.styk.martin.apkanalyzer.util.file

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.content.FileProvider
import android.util.Log
import android.widget.Toast
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.oninstall.OnInstallAppDetailActivity
import java.io.File

/**
 * @author Martin Styk
 * @version 04.01.2018.
 */
object AppOperations {

    fun installPackage(context: Context, packagePath: String) {
        val intent = Intent(Intent.ACTION_VIEW)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        try {
            val apkUri = FileProvider.getUriForFile(context, GenericFileProvider.AUTHORITY, File(packagePath))
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
        } catch (e: Exception) {
            Log.e(OnInstallAppDetailActivity::class.java.simpleName, e.toString())
            Toast.makeText(context, context.getString(R.string.install_failed), Toast.LENGTH_LONG).show()
            return
        }

        context.startActivity(intent)
    }

    fun openAppSystemPage(context: Context, packageName: String) {
        val systemInfoIntent = Intent()
        systemInfoIntent.action = android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        systemInfoIntent.data = Uri.parse("package:$packageName")

        context.startActivity(systemInfoIntent)
    }

    fun shareApkFile(context: Context, pathToApk: String) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(File(pathToApk)))
        shareIntent.type = "application/vnd.android.package-archive"

        try {
            context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_apk_using)))
        } catch (e: ActivityNotFoundException) {
            // this might happen on Android 4.4
            Toast.makeText(context, context.getString(R.string.activity_not_found_sharing), Toast.LENGTH_LONG).show()
        }

    }

    @JvmStatic
    fun openGooglePlay(context: Context, packageName: String) {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
        } catch (anfe: ActivityNotFoundException) {
            // Google Play not installed, open it in browser
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
        }
    }

    @JvmStatic
    fun startForeignActivity(context: Context, packageName: String, activityName: String) {
        val intent = Intent()
        intent.component = ComponentName(packageName, activityName)
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, R.string.activity_run_failed, Toast.LENGTH_SHORT).show()
        }
    }
}
