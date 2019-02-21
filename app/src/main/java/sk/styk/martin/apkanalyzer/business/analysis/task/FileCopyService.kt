package sk.styk.martin.apkanalyzer.business.analysis.task

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.ui.activity.MainActivity
import sk.styk.martin.apkanalyzer.util.file.FileUtils
import java.io.File
import java.io.IOException

/**
 * Async file copy as a foreground service
 *
 * @author Martin Styk
 * @version 21.02.2019.
 */
class FileCopyService : ApkAnalyzerForegroundService() {

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

                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    createNotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_ID)

                var notification = prepareNotification(shownName, targetPath, true)
                startForeground(NOTIFICATION__ID, notification.build())

                val source = File(sourcePath)
                val target = File(targetPath)
                val directory = target.parentFile

                try {
                    if (!directory.exists()) {
                        directory.mkdirs()
                    }

                    FileUtils.copy(source, target, object : FileUtils.CopyProgress {
                        override fun onProgressChanged(progress: Int) {
                            notificationManager.notify(
                                    NOTIFICATION__ID, notification
                                    .setProgress(100, progress, false)
                                    .build()
                            )
                        }
                    })
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                notification = prepareNotification(shownName, targetPath, false)
                stopForeground(false)
                stopSelf()

                notificationManager.notify(NOTIFICATION__ID, notification.build())

            }
        }.start()

        return Service.START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        // Used only in case of bound services.
        return null
    }

    private fun prepareNotification(
            name: String,
            exportPath: String?,
            inProgress: Boolean
    ): NotificationCompat.Builder {
        val title = if (inProgress) getString(
                R.string.copy_apk_background,
                name
        ) else getString(R.string.copy_apk_background_done, name)

        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(title)
                .setTicker(title)
                .setContentText(if (!inProgress && exportPath != null) FileUtils.toRelativePath(exportPath) else "")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .setOngoing(inProgress)

        if (!inProgress) {
            val intent = Intent().apply {
                action = Intent.ACTION_GET_CONTENT
                setDataAndType(Uri.parse(FileUtils.externalAppsDirectory.absolutePath), "resource/folder")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            val stackBuilder = TaskStackBuilder.create(this@FileCopyService).apply {
                addParentStack(MainActivity::class.java)
                addNextIntent(intent)
            }
            val resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            builder.addAction(R.drawable.ic_filter_list, getString(R.string.action_show), resultPendingIntent)
            builder.setContentIntent(resultPendingIntent)
        }

        return builder
    }

    companion object {

        const val SOURCE_FILE = "s_file"
        const val TARGET_FILE = "t_file"
        const val SHOWN_NAME = "shown_name"

        private const val NOTIFICATION_CHANNEL_ID = "my_channel_file_copy_service"
        private const val NOTIFICATION__ID = 9875

        fun startService(context: Context, data: AppDetailData): String {
            val source = File(data.generalData.apkDirectory)
            val target = File(FileUtils.externalAppsDirectory, "${data.generalData.packageName}_${data.generalData.versionName}.apk")

            val intent = Intent(context, FileCopyService::class.java)
            intent.putExtra(SOURCE_FILE, source.absolutePath)
            intent.putExtra(TARGET_FILE, target.absolutePath)
            intent.putExtra(SHOWN_NAME, data.generalData.applicationName)

            ContextCompat.startForegroundService(context, intent)

            return target.absolutePath
        }
    }

}