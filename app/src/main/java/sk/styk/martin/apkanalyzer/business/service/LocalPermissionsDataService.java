package sk.styk.martin.apkanalyzer.business.service;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

import sk.styk.martin.apkanalyzer.model.detail.PermissionData;
import sk.styk.martin.apkanalyzer.model.detail.UsedPermissionData;

/**
 * @author Martin Styk
 * @version 13.01.2018
 */
public class LocalPermissionsDataService {

    private PackageManager packageManager;

    private PermissionsService permissionsService;

    public LocalPermissionsDataService(PackageManager packageManager) {
        this.packageManager = packageManager;
        this.permissionsService = new PermissionsService();
    }

    public List<UsedPermissionData> get(String packageName) {
        List<UsedPermissionData> usedPermissionData;
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            usedPermissionData = permissionsService.getUsedPermissions(packageInfo, packageManager);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (usedPermissionData == null)
            return null;


        return usedPermissionData;
    }

}
