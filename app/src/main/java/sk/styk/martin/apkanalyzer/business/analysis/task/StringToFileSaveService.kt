package sk.styk.martin.apkanalyzer.business.analysis.task

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.util.file.FileUtils
import java.io.File
import java.io.IOException

class StringToFileSaveService : ApkAnalyzerForegroundService() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        object : Thread() {
            override fun run() {
                val sourceString = intent.getStringExtra(SOURCE_STRING)
                val targetPath = intent.getStringExtra(TARGET_FILE)
                val packageName = intent.getStringExtra(SOURCE_PACKAGE_NAME)

                if (sourceString == null || targetPath == null || packageName == null) {
                    stopForeground(true)
                    stopSelf()
                    return
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    createNotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_ID)
                
                var notification = prepareNotification(packageName, targetPath, true).build()
                startForeground(NOTIFICATION__ID, notification)

                try {
                    FileUtils.writeString(sourceString, targetPath)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                notification = prepareNotification(packageName, targetPath, false).build()
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(NOTIFICATION__ID, notification)

                stopForeground(false)
                stopSelf()
            }
        }.start()

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        // Used only in case of bound services.
        return null
    }

    private fun prepareNotification(packageName: String, exportPath: String?, inProgress: Boolean): NotificationCompat.Builder {
        val title = if (inProgress)
            getString(R.string.save_manifest_background_notification_title, packageName)
        else
            getString(R.string.save_manifest_background_notification_title_done, packageName)

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(title)
                .setTicker(title)
                .setContentText(exportPath)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(resources,
                        R.drawable.ic_launcher_web))
                .setOngoing(inProgress)
    }

    companion object {

        const val SOURCE_STRING = "s_string"
        const val TARGET_FILE = "t_file"
        const val SOURCE_PACKAGE_NAME = "s_package_name"

        private const val NOTIFICATION_CHANNEL_ID = "my_channel_string_to_file_save_service"
        private const val NOTIFICATION__ID = 9874

        /**
         * Starts service for exporting android manifest.
         * Service is started as a foreground service.
         * Method handles file names
         *
         * @return target file absolute path
         */
        fun startService(context: Context, packageName: String, versionCode: Int, versionName : String?, manifestContent: String): String {
            val target = File(Environment.getExternalStorageDirectory(), "${packageName}_${versionName}_${versionCode}_AndroidManifest.xml")

            val intent = Intent(context, StringToFileSaveService::class.java)
            intent.putExtra(SOURCE_STRING, manifestContent)
            intent.putExtra(TARGET_FILE, target.absolutePath)
            intent.putExtra(SOURCE_PACKAGE_NAME, packageName)

            ContextCompat.startForegroundService(context, intent)

            return target.absolutePath
        }
    }


}