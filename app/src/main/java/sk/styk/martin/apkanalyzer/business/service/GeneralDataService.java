package sk.styk.martin.apkanalyzer.business.service;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.support.annotation.NonNull;

import sk.styk.martin.apkanalyzer.model.GeneralData;

/**
 * Created by Martin Styk on 30.06.2017.
 */
public class GeneralDataService {

    private PackageManager packageManager;

    public GeneralDataService(PackageManager packageManager) {
        this.packageManager = packageManager;
    }

    public GeneralData get(@NonNull PackageInfo packageInfo) {

        ApplicationInfo applicationInfo = packageInfo.applicationInfo;

        GeneralData generalData = new GeneralData();

        generalData.setPackageName(packageInfo.packageName);
        generalData.setVersionCode(packageInfo.versionCode);
        generalData.setVersionName(packageInfo.versionName);
        generalData.setInstallLocation(resolveInstallLocation(packageInfo.installLocation));
        generalData.setFirstInstallTime(packageInfo.firstInstallTime);
        generalData.setLastUpdateTime(packageInfo.lastUpdateTime);

        if (applicationInfo != null) {
            generalData.setIcon(applicationInfo.loadIcon(packageManager));
            CharSequence description = applicationInfo.loadLabel(packageManager);
            generalData.setDescription(description !=null ? description.toString() : null);
            CharSequence label = applicationInfo.loadLabel(packageManager);
            generalData.setApplicationName(label != null ? label.toString() : applicationInfo.packageName);
            generalData.setProcessName(applicationInfo.processName);
            generalData.setSystemApp((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
            generalData.setApkDirectory(applicationInfo.sourceDir);
            generalData.setDataDirectory(applicationInfo.dataDir);
            generalData.setMinSdkVersion(getMinSdkVersion(packageInfo.packageName));
            generalData.setMinSdkLabel(resolveVersion(generalData.getMinSdkVersion()));
            generalData.setTargetSdkVersion(applicationInfo.targetSdkVersion);
            generalData.setTargetSdkLabel(resolveVersion(applicationInfo.targetSdkVersion));
        }

        return generalData;
    }

    /**
     * It is not possible to get minSdkVersions using Android PackageManager - parse AndroidManifest of app
     */
    private int getMinSdkVersion(String packageName) {
        Resources mApk1Resources = null;
        try {
            mApk1Resources = packageManager.getResourcesForApplication(packageName);
            XmlResourceParser parser = mApk1Resources.getAssets().openXmlResourceParser("AndroidManifest.xml");
            int eventType = -1;

            while (eventType != parser.END_DOCUMENT) {
                if (eventType == XmlResourceParser.START_TAG) {
                    String tagValue = parser.getName();
                    if (tagValue.equals("uses-sdk")) {
                        return parser.getAttributeIntValue("http://schemas.android.com/apk/res/android", "minSdkVersion", 0);
                    }
                }
                eventType = parser.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private String resolveInstallLocation(int installLocation) {
        switch (installLocation) {
            case PackageInfo.INSTALL_LOCATION_AUTO:
                return "Auto";
            case PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY:
                return "Internal Only";
            case PackageInfo.INSTALL_LOCATION_PREFER_EXTERNAL:
                return "Prefer External";
            default:
                return "Unspecified";
        }
    }

    private String resolveVersion(int sdkVersion) {
        //java index from 0 - first item is sdk 1
        int index = sdkVersion - 1;
        String[] version = {
                "Android 1",
                "Android 1.1 Petit Four",
                "Android 1.5 Cupcake",
                "Android 1.6 Donut",
                "Android 2.0 Eclair",
                "Android 2.0.1 Eclair",
                "Android 2.1 Eclair",
                "Android 2.2 Froyo",
                "Android 2.3 Gingerbread",
                "Android 2.3.3 Gingerbread",
                "Android 3.0 Honeycomb",
                "Android 3.1 Honeycomb",
                "Android 3.2 Honeycomb",
                "Android 4.0 Ice Cream Sandwich",
                "Android 4.0.3 Ice Cream Sandwich",
                "Android 4.1 Jelly Bean",
                "Android 4.2 Jelly Bean",
                "Android 4.3 Jelly Bean",
                "Android 4.4 KitKat",
                "Android 4.4W KitKat",
                "Android 5.0 Lollipop",
                "Android 5.1.1 Lollipop",
                "Android 6.0 Marshmallow",
                "Android 7.0 Nougat",
                "Android 7.1.2 Nougat"
        };
        return (index >= 0 && index < version.length) ? version[index] : null;
    }

}
