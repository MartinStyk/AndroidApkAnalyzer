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
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData;
import sk.styk.martin.apkanalyzer.util.file.FileUtils;

/**
 * Async file copy as a foreground service
 * <p>
 * @author Martin Styk
 * @version 15.09.2017.
 */
public class FileCopyService extends Service {

    public static final String SOURCE_FILE = "s_file";
    public static final String TARGET_FILE = "t_file";
    public static final String SHOWN_NAME = "shown_name";


    private static final String LOG_TAG = FileCopyService.class.getSimpleName();
    private static final String NOTIFICATION_CHANNEL_ID = "my_channel_file_copy_service";
    private static final int NOTIFICATION__ID = 9875;


    /**
     * Starts service for exporting data.
     * Service is started as a foreground service.
     * Method handles file names
     *
     * @return target file absolute path
     */
    public static String startService(Context context, AppDetailData data) {
        File source = new File(data.getGeneralData().getApkDirectory());
        File target = new File(Environment.getExternalStorageDirectory(), data.getGeneralData().getPackageName() + "_" + data.getGeneralData().getVersionCode() + ".apk");

        Intent intent = new Intent(context, FileCopyService.class);
        intent.putExtra(SOURCE_FILE, source.getAbsolutePath());
        intent.putExtra(TARGET_FILE, target.getAbsolutePath());
        intent.putExtra(SHOWN_NAME, data.getGeneralData().getApplicationName());

        ContextCompat.startForegroundService(context, intent);

        return target.getAbsolutePath();
    }


    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        new Thread() {
            @Override
            public void run() {
                String sourcePath = intent.getStringExtra(SOURCE_FILE);
                String targetPath = intent.getStringExtra(TARGET_FILE);
                String shownName = intent.getStringExtra(SHOWN_NAME);

                if (sourcePath == null || targetPath == null) {
                    stopForeground(true);
                    stopSelf();
                    return;
                }

                Notification notification = prepareNotification(shownName, targetPath, true).build();
                startForeground(NOTIFICATION__ID, notification);

                File source = new File(sourcePath);
                File target = new File(targetPath);

                try {
                    FileUtils.copy(source, target);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                notification = prepareNotification(shownName, targetPath, false).build();
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

    private NotificationCompat.Builder prepareNotification(String name, String exportPath, boolean inProgress) {
        CharSequence title = inProgress ? getString(R.string.copy_apk_background, name) : getString(R.string.copy_apk_background_done, name);

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