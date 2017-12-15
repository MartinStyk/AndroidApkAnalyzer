package sk.styk.martin.apkanalyzer;

import android.app.Application;
import android.os.StrictMode;

import sk.styk.martin.apkanalyzer.business.task.upload.MultipleAppDataUploadTask;

/**
 * Created by Martin Styk on 30.10.2017.
 */

public class ApkAnalyzer extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        MultipleAppDataUploadTask.start(getApplicationContext());
    }
}