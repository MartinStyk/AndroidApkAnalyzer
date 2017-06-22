package sk.styk.martin.apkanalyzer.business.service;

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

    public AppDetailDataService(@NonNull PackageManager packageManager) {
        this.packageManager = packageManager;
        this.basicDataService = new AppBasicDataService(packageManager);
        this.certificateService = new CertificateService(packageManager);
    }

    @NonNull
    public AppDetailData get(@NonNull String packageName, @NonNull String archivePath) {
        AppDetailData data = new AppDetailData();
        data.setAppBasicData(basicDataService.get(packageName));
        data.setCertificateData(certificateService.get(archivePath));
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
