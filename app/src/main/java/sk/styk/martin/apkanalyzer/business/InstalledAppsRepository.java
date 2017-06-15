package sk.styk.martin.apkanalyzer.business;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Retrieve apps installed on device
 * <p>
 * Created by Martin Styk on 14.06.2017.
 */
public class InstalledAppsRepository {

    private Context ctx;
    private static List<ApplicationInfo> packages;

    public InstalledAppsRepository(@NonNull Context ctx) {
        this.ctx = ctx;
    }

    public List<ApplicationInfo> getAll() {
        if (packages == null) {
            PackageManager pm = ctx.getPackageManager();
            packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

            for (ApplicationInfo packageInfo : packages) {
                Log.d(TAG, "Installed package :" + packageInfo.packageName);
                Log.d(TAG, "Source dir : " + packageInfo.sourceDir);
                Log.d(TAG, "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
            }
        }
        return packages;
    }
}
