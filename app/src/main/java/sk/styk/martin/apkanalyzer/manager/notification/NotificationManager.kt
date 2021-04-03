package sk.styk.martin.apkanalyzer.manager.notification

import android.app.NotificationChannel
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.dependencyinjection.util.ApplicationScope
import sk.styk.martin.apkanalyzer.manager.resources.ResourcesManager
import sk.styk.martin.apkanalyzer.ui.main.MainActivity
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import android.app.NotificationManager as AndroidNotificationManager

private const val ICON_EXPORTED_CHANNEL_ID = "Icon export channel"
private const val ICON_EXPORTED_NOTIFICATION_ID = 1_01

@Singleton
class NotificationManager @Inject constructor(
        @ApplicationScope private val context: Context,
        private val resourcesManager: ResourcesManager,
        private val androidNotificationManager: AndroidNotificationManager,
) {

    fun showImageExportedNotification(appName: String, drawableFile: File) {
        createChannel(ICON_EXPORTED_CHANNEL_ID, resourcesManager.getString(R.string.icon_save_channel))

        val builder: NotificationCompat.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(context, ICON_EXPORTED_CHANNEL_ID)
        } else {
            NotificationCompat.Builder(context)
        }

        fun getOpenFolderPendingIntent(): PendingIntent {
            Intent().apply {
                action = Intent.ACTION_VIEW
                setDataAndType(Uri.parse(drawableFile.absolutePath), "image/png")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

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

        val notification = builder
                .setContentTitle(resourcesManager.getString(R.string.app_icon_saved, appName))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setContentIntent(getOpenFolderPendingIntent())
                .addAction(R.drawable.ic_image, resourcesManager.getString(R.string.action_show), getOpenImagePendingIntent())
                .build()

        androidNotificationManager.notify(ICON_EXPORTED_NOTIFICATION_ID, notification)
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