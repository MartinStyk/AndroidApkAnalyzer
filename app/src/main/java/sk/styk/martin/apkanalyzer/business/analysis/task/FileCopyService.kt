package sk.styk.martin.apkanalyzer.business.analysis.task

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Environment
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.util.file.FileUtils
import java.io.File
import java.io.IOException

/**
 * Async file copy as a foreground service
 *
 * @author Martin Styk
 * @version 15.09.2017.
 */
class FileCopyService : Service() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        object : Thread() {
            override fun run() {
                val sourcePath = intent.getStringExtra(SOURCE_FILE)
                val targetPath = intent.getStringExtra(TARGET_FILE)
                val shownName = intent.getStringExtra(SHOWN_NAME)

                if (sourcePath == null || targetPath == null || shownName == null) {
                    stopForeground(true)
                    stopSelf()
                    return
                }

                var notification = prepareNotification(shownName, targetPath, true).build()
                startForeground(NOTIFICATION__ID, notification)

                val source = File(sourcePath)
                val target = File(targetPath)

                try {
                    FileUtils.copy(source, target)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                notification = prepareNotification(shownName, targetPath, false).build()
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(NOTIFICATION__ID, notification)

                stopForeground(false)
                stopSelf()
            }
        }.start()

        return Service.START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        // Used only in case of bound services.
        return null
    }

    private fun prepareNotification(name: String, exportPath: String?, inProgress: Boolean): NotificationCompat.Builder {
        val title = if (inProgress) getString(R.string.copy_apk_background, name) else getString(R.string.copy_apk_background_done, name)

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

        const val SOURCE_FILE = "s_file"
        const val TARGET_FILE = "t_file"
        const val SHOWN_NAME = "shown_name"

        private const val NOTIFICATION_CHANNEL_ID = "my_channel_file_copy_service"
        private const val NOTIFICATION__ID = 9875


        /**
         * Starts service for exporting data.
         * Service is started as a foreground service.
         * Method handles file names
         *
         * @return target file absolute path
         */
        fun startService(context: Context, data: AppDetailData): String {
            val source = File(data.generalData.apkDirectory)
            val target = File(Environment.getExternalStorageDirectory(), "${data.generalData.packageName}_${data.generalData.versionName}_${data.generalData.versionCode}.apk")

            val intent = Intent(context, FileCopyService::class.java)
            intent.putExtra(SOURCE_FILE, source.absolutePath)
            intent.putExtra(TARGET_FILE, target.absolutePath)
            intent.putExtra(SHOWN_NAME, data.generalData.applicationName)

            ContextCompat.startForegroundService(context, intent)

            return target.absolutePath
        }
    }
}
