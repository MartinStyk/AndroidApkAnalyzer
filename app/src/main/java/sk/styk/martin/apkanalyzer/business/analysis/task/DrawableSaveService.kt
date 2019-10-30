package sk.styk.martin.apkanalyzer.business.analysis.task

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.util.file.FileUtils
import java.io.File
import java.io.IOException

class DrawableSaveService : ApkAnalyzerForegroundService() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        object : Thread() {
            override fun run() {
                val targetPath = intent.getStringExtra(TARGET_FILE)
                val packageName = intent.getStringExtra(SOURCE_PACKAGE_NAME)
                val icon = bitmap

                if (icon == null || targetPath == null || packageName == null) {
                    stopForeground(true)
                    stopSelf()
                    return
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    createNotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_ID)

                var notification = prepareNotification(packageName, targetPath, true).build()
                startForeground(NOTIFICATION__ID, notification)

                try {
                    FileUtils.writeBitmap(icon, targetPath)
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
            getString(R.string.save_icon_background, packageName)
        else
            getString(R.string.save_icon_background_done, packageName)

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

        const val TARGET_FILE = "t_file"
        const val SOURCE_PACKAGE_NAME = "s_package_name"

        private const val NOTIFICATION_CHANNEL_ID = "my_channel_image_to_file_save_service"
        private const val NOTIFICATION__ID = 9898

        var bitmap: Bitmap? = null

        /**
         * Starts service for exporting android manifest.
         * Service is started as a foreground service.
         * Method handles file names
         *
         * @return target file absolute path
         */
        fun startService(context: Context, data: AppDetailData, bitmap: Bitmap?): String {
            if (bitmap == null) {
                return ""
            }
            val target = File(Environment.getExternalStorageDirectory(), "${data.generalData.packageName}_${data.generalData.versionName}_${data.generalData.versionCode}_icon.png")

            // do not pass it in intent as it might fail on TransacitonTooLargeException
            DrawableSaveService.bitmap = bitmap

            val intent = Intent(context, DrawableSaveService::class.java)
            intent.putExtra(TARGET_FILE, target.absolutePath)
            intent.putExtra(SOURCE_PACKAGE_NAME, data.generalData.packageName)

            ContextCompat.startForegroundService(context, intent)

            return target.absolutePath
        }
    }


}