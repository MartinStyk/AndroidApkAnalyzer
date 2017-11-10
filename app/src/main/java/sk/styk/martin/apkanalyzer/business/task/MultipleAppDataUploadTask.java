package sk.styk.martin.apkanalyzer.business.task;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import sk.styk.martin.apkanalyzer.business.service.AppBasicDataService;
import sk.styk.martin.apkanalyzer.business.service.AppDetailDataService;
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

    @Override
    protected void onHandleIntent(Intent intent) {

        if (!ConnectivityHelper.hasInternetAccess(getApplicationContext()))
            return;

        List<AppListData> apps = new AppBasicDataService(getPackageManager()).getForSources(AppSource.AMAZON_STORE, AppSource.GOOGLE_PLAY, AppSource.UNKNOWN);
        AppDetailDataService detailDataService = new AppDetailDataService(getPackageManager());

        for (AppListData app : apps) {
            AppDetailData appDetailData = detailDataService.get(app.getPackageName(), null);
            AppDataSaveTask appDataSaveTask = new AppDataSaveTask(this);
            appDataSaveTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, appDetailData);
        }

    }
}
