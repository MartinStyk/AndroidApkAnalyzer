package sk.styk.martin.apkanalyzer.business.service;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sk.styk.martin.apkanalyzer.model.AppBasicInfo;
import sk.styk.martin.apkanalyzer.util.AppBasicInfoComparator;

/**
 * Retrieve apps installed on device
 * <p>
 * Created by Martin Styk on 14.06.2017.
 */
public class InstalledAppsService {

    private PackageManager packageManager;
    private List<AppBasicInfo> packages;

    public InstalledAppsService(@NonNull PackageManager packageManager) {
        this.packageManager = packageManager;
    }

    public InstalledAppsService(@NonNull Context context) {
        this.packageManager = context.getPackageManager();
    }

    @NonNull
    public List<AppBasicInfo> getAll() {

        List<ApplicationInfo> applications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        packages = new ArrayList<>(applications.size());

        for (ApplicationInfo applicationInfo : applications) {
            AppBasicInfo appBasicInfo = new AppBasicInfo();
            appBasicInfo.setPackageName(applicationInfo.packageName);
            appBasicInfo.setApplicationName(loadLabel(applicationInfo));
            appBasicInfo.setPathToApk(applicationInfo.sourceDir);
            appBasicInfo.setIcon(applicationInfo.loadIcon(packageManager));
            appBasicInfo.setSystemApp((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
            packages.add(appBasicInfo);
        }

        Collections.sort(packages, AppBasicInfoComparator.INSTANCE);

        return packages;
    }

    private String loadLabel(ApplicationInfo applicationInfo) {

        CharSequence label = applicationInfo.loadLabel(packageManager);
        String finalResult = label != null ? label.toString() : applicationInfo.packageName;

        return finalResult;
    }

}
