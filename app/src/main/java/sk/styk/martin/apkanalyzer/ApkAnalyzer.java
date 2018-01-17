package sk.styk.martin.apkanalyzer;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import sk.styk.martin.apkanalyzer.business.task.upload.MultipleAppDataUploadService;
import sk.styk.martin.apkanalyzer.util.FirstStartHelper;

/**
 * @author Martin Styk
 * @version 30.10.2017.
 */
public class ApkAnalyzer extends Application {

    private static ApkAnalyzer instance;

    public static Context getContext(){
        return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if (!FirstStartHelper.isFirstStart(getApplicationContext()))
            MultipleAppDataUploadService.start(getApplicationContext());
    }
}