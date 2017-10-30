package sk.styk.martin.apkanalyzer;

import android.app.Application;
import android.os.StrictMode;

/**
 * Created by Martin Styk on 30.10.2017.
 */

public class ApkAnalyzer extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }
}