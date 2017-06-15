package sk.styk.martin.apkanalyzer.business.service;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Retrieve apps installed on device
 * <p>
 * Created by Martin Styk on 14.06.2017.
 */
public class InstalledAppsService {

    private Context ctx;
    private static List<ApplicationInfo> packages;

    public InstalledAppsService(@NonNull Context ctx) {
        this.ctx = ctx;
    }

    public List<ApplicationInfo> getAll() {
        if (packages == null) {
            PackageManager pm = ctx.getPackageManager();
            packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        }
        return packages;
    }
}
