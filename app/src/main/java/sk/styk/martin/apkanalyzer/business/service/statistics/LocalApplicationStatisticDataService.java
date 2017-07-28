package sk.styk.martin.apkanalyzer.business.service.statistics;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import sk.styk.martin.apkanalyzer.business.service.CertificateService;
import sk.styk.martin.apkanalyzer.business.service.FileDataService;
import sk.styk.martin.apkanalyzer.business.service.GeneralDataService;
import sk.styk.martin.apkanalyzer.business.service.ResourceService;
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
        this.certificateService = new CertificateService(packageManager);
        this.fileService = new FileDataService(packageManager);
        this.resourceService = new ResourceService(packageManager);
    }

    public LocalStatisticsAppData get(String packageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES |
                            PackageManager.GET_ACTIVITIES |
                            PackageManager.GET_SERVICES |
                            PackageManager.GET_PROVIDERS |
                            PackageManager.GET_RECEIVERS |
                            PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        ApplicationInfo applicationInfo = packageInfo.applicationInfo;

        if (packageInfo == null || applicationInfo == null)
            return null;

        LocalStatisticsAppData data = new LocalStatisticsAppData();
        data.setSystemApp((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
        data.setInstallLocation(packageInfo.installLocation);
        data.setTargetSdk(applicationInfo.targetSdkVersion);
        data.setMinSdk(generalDataService.getMinSdkVersion(packageName));
        data.setApkSize(generalDataService.getApkSize(applicationInfo.sourceDir));

        data.setSignAlgorithm(certificateService.getSignAlgorithm(packageInfo));

        data.setActivites(packageInfo.activities.length);
        data.setServices(packageInfo.services.length);
        data.setProviders(packageInfo.providers.length);
        data.setReceivers(packageInfo.receivers.length);

        data.setDefinedPermissions(packageInfo.permissions.length);
        data.setUsedPermissions(packageInfo.requestedPermissions.length);

        FileData fileData = fileService.get(packageInfo);
        data.setFiles(fileData.getAllHashes().size());

        ResourceData resourceData = resourceService.get(fileData);
        data.setDrawables(resourceData.getDrawables());
        data.setDifferentDrawables(resourceData.getDifferentDrawables());
        data.setLayouts(resourceData.getLayouts());
        data.setDifferentLayouts(resourceData.getDifferentLayouts());

        return data;
    }

}
