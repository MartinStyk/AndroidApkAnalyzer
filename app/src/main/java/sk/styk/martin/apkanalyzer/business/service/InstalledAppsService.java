package sk.styk.martin.apkanalyzer.business.service;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import sk.styk.martin.apkanalyzer.model.AppBasicInfo;

/**
 * Retrieve apps installed on device
 * <p>
 * Created by Martin Styk on 14.06.2017.
 */
public class InstalledAppsService {

    private Context ctx;
    private static List<AppBasicInfo> packages;

    public InstalledAppsService(@NonNull Context ctx) {
        this.ctx = ctx;
    }

    public List<AppBasicInfo> getAll() {
        if (packages == null) {
            PackageManager pm = ctx.getPackageManager();
            List<ApplicationInfo> applications = pm.getInstalledApplications(PackageManager.GET_META_DATA);

            packages = new ArrayList<>(applications.size());

            for (ApplicationInfo applicationInfo : applications) {
                AppBasicInfo appBasicInfo = new AppBasicInfo();
                appBasicInfo.setPackageName(applicationInfo.packageName);
                appBasicInfo.setApplicationName(applicationInfo.name);
                appBasicInfo.setPathToApk(applicationInfo.sourceDir);
                appBasicInfo.setIcon(applicationInfo.loadIcon(pm));
                packages.add(appBasicInfo);
            }
        }
        return packages;
    }
}
