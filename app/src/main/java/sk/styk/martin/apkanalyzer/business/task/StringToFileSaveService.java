package sk.styk.martin.apkanalyzer.business.task;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import java.io.File;
import java.io.IOException;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.util.file.FileUtils;

/**
 * Async save of string to file
 *
 * Created by Martin Styk on 15.09.2017.
 */
public class StringToFileSaveService extends Service {

    public static final String SOURCE_STRING = "s_string";
    public static final String TARGET_FILE = "t_file";
    public static final String SOURCE_PACKAGE_NAME = "s_package_name";

    private static final String NOTIFICATION_CHANNEL_ID = "my_channel_string_to_file_save_service";
    private static final int NOTIFICATION__ID = 9874;
    
    /**
     * Starts service for exporting android manifest.
     * Service is started as a foreground service.
     * Method handles file names
     *
     * @return target file absolute path
     */
    public static String startService(Context context, String packageName, String manifestContent) {
        File target = new File(Environment.getExternalStorageDirectory(), "AndroidManifest_" + packageName + ".xml");

        Intent intent = new Intent(context, StringToFileSaveService.class);
        intent.putExtra(StringToFileSaveService.SOURCE_STRING, manifestContent);
        intent.putExtra(StringToFileSaveService.TARGET_FILE, target.getAbsolutePath());
        intent.putExtra(StringToFileSaveService.SOURCE_PACKAGE_NAME, packageName);

        ContextCompat.startForegroundService(context, intent);

        return target.getAbsolutePath();
    }


    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        new Thread() {
            @Override
            public void run() {
                String sourceString = intent.getStringExtra(SOURCE_STRING);
                String targetPath = intent.getStringExtra(TARGET_FILE);
                String packageName = intent.getStringExtra(SOURCE_PACKAGE_NAME);

                if (sourceString == null || targetPath == null) {
                    stopForeground(true);
                    stopSelf();
                    return;
                }

                Notification notification = prepareNotification(packageName, targetPath, true).build();
                startForeground(NOTIFICATION__ID, notification);

                try {
                    FileUtils.writeString(sourceString, targetPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                notification = prepareNotification(packageName, targetPath, false).build();
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (notificationManager != null)
                    notificationManager.notify(NOTIFICATION__ID, notification);

                stopForeground(false);
                stopSelf();
            }
        }.start();

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case of bound services.
        return null;
    }

    private NotificationCompat.Builder prepareNotification(String packageName, String exportPath, boolean inProgress) {
        CharSequence title = inProgress ? getString(R.string.save_manifest_background_notification_title, packageName)
                : getString(R.string.save_manifest_background_notification_title_done, packageName);

        return new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(title)
                .setTicker(title)
                .setContentText(exportPath)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.drawable.ic_launcher_web))
                .setOngoing(inProgress);
    }


}