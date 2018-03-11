package sk.styk.martin.apkanalyzer.business.analysis.task

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.util.file.FileUtils
import java.io.File
import java.io.IOException

/**
 * Async save of drawable to file
 *
 * @author Martin Styk
 * @version 15.09.2017.
 */
class DrawableSaveService : Service() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        object : Thread() {
            override fun run() {
                val icon = intent.getParcelableExtra<Bitmap>(SOURCE_PIC)
                val targetPath = intent.getStringExtra(TARGET_FILE)
                val packageName = intent.getStringExtra(SOURCE_PACKAGE_NAME)

                if (icon == null || targetPath == null || packageName == null) {
                    stopForeground(true)
                    stopSelf()
                    return
                }

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

        return Service.START_NOT_STICKY
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

        const val SOURCE_PIC = "s_string"
        const val TARGET_FILE = "t_file"
        const val SOURCE_PACKAGE_NAME = "s_package_name"

        private const val NOTIFICATION_CHANNEL_ID = "my_channel_image_to_file_save_service"
        private const val NOTIFICATION__ID = 9898

        /**
         * Starts service for exporting android manifest.
         * Service is started as a foreground service.
         * Method handles file names
         *
         * @return target file absolute path
         */
        fun startService(context: Context, packageName: String, bitmap : Bitmap?): String {
            if(bitmap == null){
                return ""
            }
            val target = File(Environment.getExternalStorageDirectory(), "icon_$packageName.png")

            val intent = Intent(context, DrawableSaveService::class.java)
            intent.putExtra(SOURCE_PIC, bitmap)
            intent.putExtra(TARGET_FILE, target.absolutePath)
            intent.putExtra(SOURCE_PACKAGE_NAME, packageName)

            ContextCompat.startForegroundService(context, intent)

            return target.absolutePath
        }
    }


}