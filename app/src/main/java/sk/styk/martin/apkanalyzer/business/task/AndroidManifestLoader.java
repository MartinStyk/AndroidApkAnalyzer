package sk.styk.martin.apkanalyzer.business.task;

import android.content.Context;
import android.content.pm.PackageManager;

import sk.styk.martin.apkanalyzer.business.service.AndroidManifestService;

/**
 * Loader async task for loadinf android manifest content
 * <p>
 * @author Martin Styk
 * @version 15.09.2017.
 */
public class AndroidManifestLoader extends ApkAnalyzerAbstractAsyncLoader<String> {

    public static final int ID = 4;

    private PackageManager packageManager;
    private String packageName;

    public AndroidManifestLoader(Context context, String packageName) {
        super(context);
        this.packageManager = context.getPackageManager();
        this.packageName = packageName;
    }

    @Override
    public String loadInBackground() {
        return new AndroidManifestService(packageManager, packageName).loadAndroidManifest();
    }


}

