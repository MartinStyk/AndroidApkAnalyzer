package sk.styk.martin.apkanalyzer.business.service;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sk.styk.martin.apkanalyzer.model.AppBasicData;
import sk.styk.martin.apkanalyzer.util.AppBasicInfoComparator;

/**
 * Retrieve apps installed on device
 * <p>
 * Created by Martin Styk on 14.06.2017.
 */
public class AppBasicDataService {

    private PackageManager packageManager;

    public AppBasicDataService(@NonNull PackageManager packageManager) {
        this.packageManager = packageManager;
    }

    public AppBasicDataService(@NonNull Context context) {
        this.packageManager = context.getPackageManager();
    }

    @NonNull
    public List<AppBasicData> getAll() {

        List<ApplicationInfo> applications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        List<AppBasicData> packages = new ArrayList<>(applications.size());

        for (ApplicationInfo applicationInfo : applications) {
            AppBasicData appBasicData = new AppBasicData();
            appBasicData.setPackageName(applicationInfo.packageName);
            appBasicData.setApplicationName(loadLabel(applicationInfo));
            appBasicData.setPathToApk(applicationInfo.sourceDir);
            appBasicData.setIcon(applicationInfo.loadIcon(packageManager));
            appBasicData.setSystemApp((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
            packages.add(appBasicData);
        }

        Collections.sort(packages, AppBasicInfoComparator.INSTANCE);

        return packages;
    }

    @NonNull
    public AppBasicData get(@NonNull String packageName) {

        ApplicationInfo applicationInfo;

        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }

        AppBasicData appBasicData = new AppBasicData();
        appBasicData.setPackageName(applicationInfo.packageName);
        appBasicData.setApplicationName(loadLabel(applicationInfo));
        appBasicData.setPathToApk(applicationInfo.sourceDir);
        appBasicData.setIcon(applicationInfo.loadIcon(packageManager));
        appBasicData.setSystemApp((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);

        return appBasicData;
    }

    private String loadLabel(ApplicationInfo applicationInfo) {

        CharSequence label = applicationInfo.loadLabel(packageManager);
        String finalResult = label != null ? label.toString() : applicationInfo.packageName;

        return finalResult;
    }

}
