package sk.styk.martin.apkanalyzer.business.service;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import sk.styk.martin.apkanalyzer.model.detail.AppDetailData;

/**
 * Retrieve apps installed on device
 * <p>
 * Created by Martin Styk on 14.06.2017.
 */
public class AppDetailDataService {

    private final int ANALYSIS_FLAGS = PackageManager.GET_SIGNATURES |
            PackageManager.GET_ACTIVITIES |
            PackageManager.GET_SERVICES |
            PackageManager.GET_PROVIDERS |
            PackageManager.GET_RECEIVERS |
            PackageManager.GET_PERMISSIONS;

    private PackageManager packageManager;

    private GeneralDataService generalDataService;
    private CertificateService certificateService;
    private AppComponentsService appComponentsService;
    private PermissionsService permissionsService;
    private FileDataService fileDataService;
    private ResourceService resourceService;

    public AppDetailDataService(@NonNull PackageManager packageManager) {
        this.packageManager = packageManager;
        this.generalDataService = new GeneralDataService(packageManager);
        this.certificateService = new CertificateService();
        this.appComponentsService = new AppComponentsService(packageManager);
        this.permissionsService = new PermissionsService();
        this.fileDataService = new FileDataService();
        this.resourceService = new ResourceService();
    }


    public AppDetailData get(String packageName, String pathToPackage) {
        PackageInfo packageInfo = null;
        AppDetailData data = null;
        // decide whether we analyze installed app or only apk file
        if (packageName != null && pathToPackage == null) {
            data = new AppDetailData(AppDetailData.AnalysisMode.INSTALLED_PACKAGE);

            try {
                packageInfo = packageManager.getPackageInfo(packageName, ANALYSIS_FLAGS);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        } else if (packageName == null && pathToPackage != null) {
            data = new AppDetailData(AppDetailData.AnalysisMode.APK_FILE);

            packageInfo = packageManager.getPackageArchiveInfo(pathToPackage, ANALYSIS_FLAGS);
            if (packageInfo != null) packageInfo.applicationInfo.sourceDir = pathToPackage;
        } else {
            throw new RuntimeException("Only one of parameters can be specified - packageName = " + packageName +
                    "pathToPackage = " + pathToPackage);
        }

        if (packageInfo == null)
            return null;

        data.setGeneralData(generalDataService.get(packageInfo));
        data.setCertificateData(certificateService.get(packageInfo));
        data.setActivityData(appComponentsService.getActivities(packageInfo));
        data.setServiceData(appComponentsService.getServices(packageInfo));
        data.setContentProviderData(appComponentsService.getContentProviders(packageInfo));
        data.setBroadcastReceiverData(appComponentsService.getBroadcastReceivers(packageInfo));
        data.setPermissionData(permissionsService.get(packageInfo));
        data.setFileData(fileDataService.get(packageInfo));
        data.setResourceData(resourceService.get(data.getFileData()));

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
