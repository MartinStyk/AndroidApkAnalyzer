package sk.styk.martin.apkanalyzer.business.service;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.support.annotation.NonNull;

import java.io.File;

import sk.styk.martin.apkanalyzer.model.detail.GeneralData;
import sk.styk.martin.apkanalyzer.util.AndroidVersionHelper;
import sk.styk.martin.apkanalyzer.util.InstallLocationHelper;

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
        generalData.setInstallLocation(InstallLocationHelper.resolveInstallLocation(packageInfo.installLocation));
        generalData.setFirstInstallTime(packageInfo.firstInstallTime);
        generalData.setLastUpdateTime(packageInfo.lastUpdateTime);

        if (applicationInfo != null) {
            generalData.setIcon(applicationInfo.loadIcon(packageManager));
            CharSequence description = applicationInfo.loadDescription(packageManager);
            generalData.setDescription(description != null ? description.toString() : null);
            CharSequence label = applicationInfo.loadLabel(packageManager);
            generalData.setApplicationName(label != null ? label.toString() : applicationInfo.packageName);
            generalData.setProcessName(applicationInfo.processName);
            generalData.setSystemApp((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
            generalData.setApkDirectory(applicationInfo.sourceDir);
            generalData.setApkSize(getApkSize(applicationInfo.sourceDir));
            generalData.setDataDirectory(applicationInfo.dataDir);
            generalData.setMinSdkVersion(getMinSdkVersion(packageInfo.packageName));
            generalData.setMinSdkLabel(AndroidVersionHelper.resolveVersion(generalData.getMinSdkVersion()));
            generalData.setTargetSdkVersion(applicationInfo.targetSdkVersion);
            generalData.setTargetSdkLabel(AndroidVersionHelper.resolveVersion(applicationInfo.targetSdkVersion));
        }

        return generalData;
    }

    /**
     * It is not possible to get minSdkVersions using Android PackageManager - parse AndroidManifest of app
     */
    public int getMinSdkVersion(String packageName) {
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

    public long getApkSize(String sourceDir) {
        return new File(sourceDir).length();
    }

}
