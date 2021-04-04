package sk.styk.martin.apkanalyzer.manager.notification

import android.app.NotificationChannel
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.IntRange
import androidx.core.app.NotificationCompat
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.dependencyinjection.util.ApplicationScope
import sk.styk.martin.apkanalyzer.manager.resources.ResourcesManager
import sk.styk.martin.apkanalyzer.ui.main.MainActivity
import sk.styk.martin.apkanalyzer.util.file.FileUtils
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import android.app.NotificationManager as AndroidNotificationManager

private const val ICON_EXPORT_CHANNEL_ID = "Icon export channel"
private const val ICON_EXPORT_NOTIFICATION_ID = 1_01

private const val APP_EXPORT_CHANNEL_ID = "App export channel"
private const val APP_EXPORT_NOTIFICATION_ID = 1_02

private const val MANIFEST_EXPORT_CHANNEL_ID = "Manifest export channel"
private const val MANIFEST_EXPORT_NOTIFICATION_ID = 1_03

@Singleton
class NotificationManager @Inject constructor(
        @ApplicationScope private val context: Context,
        private val resourcesManager: ResourcesManager,
        private val androidNotificationManager: AndroidNotificationManager,
) {

    fun showImageExportedNotification(appName: String, drawableFile: File) {
        createChannel(ICON_EXPORT_CHANNEL_ID, resourcesManager.getString(R.string.icon_save_channel))

        fun getOpenFolderPendingIntent(): PendingIntent {
            val intent = Intent().apply {
                action = Intent.ACTION_GET_CONTENT
                setDataAndType(Uri.parse(drawableFile.parentFile!!.absolutePath), "resource/folder")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            val stackBuilder = TaskStackBuilder.create(context).apply {
                addParentStack(MainActivity::class.java)
                addNextIntent(intent)
            }
            return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        fun getOpenImagePendingIntent(): PendingIntent {
            val intent = Intent().apply {
                action = Intent.ACTION_VIEW
                setDataAndType(Uri.parse(drawableFile.absolutePath), "image/png")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            val stackBuilder = TaskStackBuilder.create(context).apply {
                addParentStack(MainActivity::class.java)
                addNextIntent(intent)
            }
            return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notification = notificationBuilder(ICON_EXPORT_CHANNEL_ID)
                .setContentTitle(resourcesManager.getString(R.string.app_icon_saved, appName))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setContentIntent(getOpenFolderPendingIntent())
                .addAction(R.drawable.ic_image, resourcesManager.getString(R.string.action_show), getOpenImagePendingIntent())
                .build()

        androidNotificationManager.notify(ICON_EXPORT_NOTIFICATION_ID, notification)
    }

    fun showAppExportProgressNotification(appName: String): NotificationCompat.Builder {
        createChannel(APP_EXPORT_CHANNEL_ID, resourcesManager.getString(R.string.app_save_channel))

        val notification = notificationBuilder(APP_EXPORT_CHANNEL_ID)
                .setContentTitle(resourcesManager.getString(R.string.saving_app, appName))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .setProgress(100, 0, false)

        androidNotificationManager.notify(APP_EXPORT_NOTIFICATION_ID, notification.build())
        return notification
    }

    fun updateAppExportProgressNotification(notificationBuilder: NotificationCompat.Builder, @IntRange(from = 0, to = 100) progress: Int): NotificationCompat.Builder {
        notificationBuilder.setProgress(100, progress, false)
        androidNotificationManager.notify(APP_EXPORT_NOTIFICATION_ID, notificationBuilder.build())
        return notificationBuilder
    }

    fun showAppExportDoneNotification(appName: String, outputFile: File) {
        cancelNotification(APP_EXPORT_NOTIFICATION_ID)
        createChannel(APP_EXPORT_CHANNEL_ID, resourcesManager.getString(R.string.app_save_channel))

        fun getOpenFolderPendingIntent(): PendingIntent {
            val intent = Intent().apply {
                action = Intent.ACTION_GET_CONTENT
                setDataAndType(Uri.parse(outputFile.parentFile!!.absolutePath), "resource/folder")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            val stackBuilder = TaskStackBuilder.create(context).apply {
                addParentStack(MainActivity::class.java)
                addNextIntent(intent)
            }
            return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notification = notificationBuilder(APP_EXPORT_CHANNEL_ID)
                .setContentTitle(resourcesManager.getString(R.string.saved_app, appName))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(FileUtils.toRelativePath(outputFile.path))
                .setOngoing(false)
                .addAction(R.drawable.ic_get_app, resourcesManager.getString(R.string.action_show), getOpenFolderPendingIntent())
                .setContentIntent(getOpenFolderPendingIntent())
                .build()

        androidNotificationManager.notify(APP_EXPORT_NOTIFICATION_ID, notification)
    }

    fun showManifestSavedNotification(appName: String, outputFile: File) {
        createChannel(MANIFEST_EXPORT_CHANNEL_ID, resourcesManager.getString(R.string.manifest_save_channel))

        fun getOpenContentPendingIntent(): PendingIntent {
            val intent = Intent().apply {
                action = Intent.ACTION_VIEW
                setDataAndType(Uri.parse(outputFile.absolutePath), "text/xml")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            val stackBuilder = TaskStackBuilder.create(context).apply {
                addParentStack(MainActivity::class.java)
                addNextIntent(intent)
            }
            return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        fun getOpenFolderPendingIntent(): PendingIntent {
            val intent = Intent().apply {
                action = Intent.ACTION_GET_CONTENT
                setDataAndType(Uri.parse(outputFile.parentFile!!.absolutePath), "resource/folder")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            val stackBuilder = TaskStackBuilder.create(context).apply {
                addParentStack(MainActivity::class.java)
                addNextIntent(intent)
            }
            return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notification = notificationBuilder(MANIFEST_EXPORT_CHANNEL_ID)
                .setContentTitle(resourcesManager.getString(R.string.save_manifest_background_notification_title_done, appName))
                .setContentText(outputFile.path)
                .setSmallIcon(R.mipmap.ic_launcher)
                .addAction(R.drawable.ic_file, resourcesManager.getString(R.string.action_show), getOpenContentPendingIntent())
                .setContentIntent(getOpenFolderPendingIntent())
                .build()

        androidNotificationManager.notify(MANIFEST_EXPORT_NOTIFICATION_ID, notification)

    }

    private fun notificationBuilder(channelId: String) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        NotificationCompat.Builder(context, channelId)
    } else {
        NotificationCompat.Builder(context)
    }

    private fun createChannel(channelId: String, channelName: CharSequence) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, AndroidNotificationManager.IMPORTANCE_DEFAULT)
            androidNotificationManager.createNotificationChannel(channel)
        }
    }

    private fun cancelNotification(notificationId: Int) {
        androidNotificationManager.cancel(notificationId)
    }

}