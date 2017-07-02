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

    private GeneralDataService generalDataService;
    private CertificateService certificateService;
    private AppComponentsService appComponentsService;
    private PermissionsService permissionsService;
    private FileDataService fileDataService;

    public AppDetailDataService(@NonNull PackageManager packageManager) {
        this.packageManager = packageManager;
        this.generalDataService = new GeneralDataService(packageManager);
        this.certificateService = new CertificateService(packageManager);
        this.appComponentsService = new AppComponentsService(packageManager);
        this.permissionsService = new PermissionsService(packageManager);
        this.fileDataService = new FileDataService(packageManager);
    }

    @NonNull
    public AppDetailData get(@NonNull String packageName, @NonNull String archivePath) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES |
                            PackageManager.GET_ACTIVITIES |
                            PackageManager.GET_SERVICES |
                            PackageManager.GET_PROVIDERS |
                            PackageManager.GET_RECEIVERS |
                            PackageManager.GET_PERMISSIONS );
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }


        AppDetailData data = new AppDetailData();
        data.setGeneralData(generalDataService.get(packageInfo));
        data.setCertificateData(certificateService.get(packageInfo));
        data.setActivityData(appComponentsService.getActivities(packageInfo));
        data.setServiceData(appComponentsService.getServices(packageInfo));
        data.setContentProviderData(appComponentsService.getContentProviders(packageInfo));
        data.setBroadcastReceiverData(appComponentsService.getBroadcastReceivers(packageInfo));
        data.setPermissionData(permissionsService.get(packageInfo));
        data.setFileData(fileDataService.get(packageInfo));
        return data;
    }

    @Override
    public String toString() {
        return "AppDetailDataService{" +
                "packageManager=" + packageManager +
                ", generalDataService=" + generalDataService +
                ", certificateService=" + certificateService +
                ", appComponentsService=" + appComponentsService +
                ", permissionsService=" + permissionsService +
                '}';
    }
}
