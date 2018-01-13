package sk.styk.martin.apkanalyzer.business.service;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import sk.styk.martin.apkanalyzer.business.service.AndroidManifestService;
import sk.styk.martin.apkanalyzer.business.service.CertificateService;
import sk.styk.martin.apkanalyzer.business.service.FileDataService;
import sk.styk.martin.apkanalyzer.business.service.GeneralDataService;
import sk.styk.martin.apkanalyzer.business.service.ResourceService;
import sk.styk.martin.apkanalyzer.model.detail.AppSource;
import sk.styk.martin.apkanalyzer.model.detail.FileData;
import sk.styk.martin.apkanalyzer.model.detail.ResourceData;
import sk.styk.martin.apkanalyzer.model.statistics.LocalStatisticsAppData;

/**
 * Created by Martin Styk on 28.07.2017.
 */
public class LocalApplicationStatisticDataService {

    private PackageManager packageManager;

    private GeneralDataService generalDataService;
    private CertificateService certificateService;
    private FileDataService fileService;
    private ResourceService resourceService;

    public LocalApplicationStatisticDataService(PackageManager packageManager) {
        this.packageManager = packageManager;
        this.generalDataService = new GeneralDataService(packageManager);
        this.certificateService = new CertificateService();
        this.fileService = new FileDataService();
        this.resourceService = new ResourceService();
    }

    public LocalStatisticsAppData get(String packageName) {
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES |
                            PackageManager.GET_ACTIVITIES |
                            PackageManager.GET_SERVICES |
                            PackageManager.GET_PROVIDERS |
                            PackageManager.GET_RECEIVERS |
                            PackageManager.GET_PERMISSIONS);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (packageInfo == null || packageInfo.applicationInfo == null)
            return null;

        ApplicationInfo applicationInfo = packageInfo.applicationInfo;

        LocalStatisticsAppData data = new LocalStatisticsAppData();
        data.setPackageName(packageName);
        data.setSystemApp((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
        data.setInstallLocation(packageInfo.installLocation);
        data.setTargetSdk(applicationInfo.targetSdkVersion);
        data.setMinSdk(AndroidManifestService.getMinSdkVersion(applicationInfo, packageManager));
        data.setApkSize(generalDataService.getApkSize(applicationInfo.sourceDir));
        data.setAppSource(AppSource.get(packageManager, packageName, data.isSystemApp()));

        data.setSignAlgorithm(certificateService.getSignAlgorithm(packageInfo));

        data.setActivities(packageInfo.activities == null ? 0 : packageInfo.activities.length);
        data.setServices(packageInfo.services == null ? 0 : packageInfo.services.length);
        data.setProviders(packageInfo.providers == null ? 0 : packageInfo.providers.length);
        data.setReceivers(packageInfo.receivers == null ? 0 : packageInfo.receivers.length);

        data.setDefinedPermissions(packageInfo.permissions == null ? 0 : packageInfo.permissions.length);
        data.setUsedPermissions(packageInfo.requestedPermissions == null ? 0 : packageInfo.requestedPermissions.length);

        FileData fileData = fileService.get(packageInfo);
        data.setFiles(fileData.getMenuHashes().size() +
                fileData.getDrawableHashes().size() +
                fileData.getLayoutHashes().size() +
                fileData.getOtherHashes().size() + 2); // +2 for arsc and dex

        ResourceData resourceData = resourceService.get(fileData);
        data.setDrawables(resourceData.getDrawables());
        data.setDifferentDrawables(resourceData.getDifferentDrawables());
        data.setLayouts(resourceData.getLayouts());
        data.setDifferentLayouts(resourceData.getDifferentLayouts());

        return data;
    }

}
