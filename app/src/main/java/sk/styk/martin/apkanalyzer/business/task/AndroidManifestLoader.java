package sk.styk.martin.apkanalyzer.business.task;

import android.content.Context;
import android.content.pm.PackageManager;

import sk.styk.martin.apkanalyzer.business.service.AndroidManifestService;
import sk.styk.martin.apkanalyzer.business.service.AppDetailDataService;

/**
 * Loader async task for loadinf android manifest content
 * <p>
 * Created by Martin Styk on 15.09.2017.
 */
public class AndroidManifestLoader extends ApkAnalyzerAbstractAsyncLoader<String> {

    public static final int ID = 4;
    private final AppDetailDataService appDetailDataService;

    private PackageManager packageManager;
    private String packageName;

    public AndroidManifestLoader(Context context, String packageName) {
        super(context);
        this.packageManager = context.getPackageManager();
        this.packageName = packageName;
        appDetailDataService = new AppDetailDataService(context.getPackageManager());
    }

    @Override
    public String loadInBackground() {
        return new AndroidManifestService().loadAndroidManifest(packageManager, packageName);
    }


}

