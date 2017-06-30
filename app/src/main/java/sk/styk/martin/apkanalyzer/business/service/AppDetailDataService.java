package sk.styk.martin.apkanalyzer.business.service;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import sk.styk.martin.apkanalyzer.model.AppDetailData;

/**
 * Retrieve apps installed on device
 * <p>
 * Created by Martin Styk on 14.06.2017.
 */
public class AppDetailDataService {

    private PackageManager packageManager;

    private AppBasicDataService basicDataService;
    private CertificateService certificateService;
    private AppComponentsService appComponentsService;

    public AppDetailDataService(@NonNull PackageManager packageManager) {
        this.packageManager = packageManager;
        this.basicDataService = new AppBasicDataService(packageManager);
        this.certificateService = new CertificateService(packageManager);
        this.appComponentsService = new AppComponentsService(packageManager);
    }

    @NonNull
    public AppDetailData get(@NonNull String packageName, @NonNull String archivePath) {
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(archivePath,
                PackageManager.GET_SIGNATURES |
                PackageManager.GET_ACTIVITIES |
                PackageManager.GET_SERVICES );


        AppDetailData data = new AppDetailData();
        data.setAppBasicData(basicDataService.get(packageName));
        data.setCertificateData(certificateService.get(packageInfo));
        data.setActivityData(appComponentsService.getActivities(packageInfo));
        data.setServiceData(appComponentsService.getServices(packageInfo));

        return data;
    }

    @Override
    public String toString() {
        return "AppDetailDataService{" +
                "packageManager=" + packageManager +
                ", basicDataService=" + basicDataService +
                '}';
    }
}
