package sk.styk.martin.apkanalyzer.business.task.upload;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;

import java.util.List;

import sk.styk.martin.apkanalyzer.business.service.AppBasicDataService;
import sk.styk.martin.apkanalyzer.business.service.AppDetailDataService;
import sk.styk.martin.apkanalyzer.database.service.SendDataService;
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData;
import sk.styk.martin.apkanalyzer.model.detail.AppSource;
import sk.styk.martin.apkanalyzer.model.list.AppListData;
import sk.styk.martin.apkanalyzer.util.ConnectivityHelper;

/**
 * Created by mstyk on 11/8/17.
 */

public class MultipleAppDataUploadTask extends IntentService {

    public MultipleAppDataUploadTask() {
        super(MultipleAppDataUploadTask.class.getSimpleName());
    }

    /**
     * Uploads all not uploaded not pre-installed apps to server.
     * Starts only if internet connection is available.
     * Uploads only previously not uploaded data.
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        if (!ConnectivityHelper.isUploadPossible(getApplicationContext()))
            return;

        List<AppListData> apps = new AppBasicDataService(getPackageManager()).getForSources(AppSource.AMAZON_STORE, AppSource.GOOGLE_PLAY, AppSource.UNKNOWN);
        AppDetailDataService detailDataService = new AppDetailDataService(getPackageManager());

        for (AppListData app : apps) {

            if (!SendDataService.isAlreadyUploaded(app.getPackageName(), app.getVersion(), this)) {
                AppDetailData appDetailData = detailDataService.get(app.getPackageName(), null);
                AppDataUploadTask appDataSaveTask = new AppDataUploadTask(this);
                appDataSaveTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, appDetailData);
            }

        }

    }
}
