package sk.styk.martin.apkanalyzer.business.analysis.task

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * @author Martin Styk {@literal <martin.styk@gmail.com>}
 */
abstract class ApkAnalyzerForegroundService : Service() {

    @RequiresApi(Build.VERSION_CODES.O)
    protected fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.LTGRAY
        chan.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

}