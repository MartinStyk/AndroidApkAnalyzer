package sk.styk.martin.apkanalyzer.business.service;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Build;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import sk.styk.martin.apkanalyzer.model.detail.PermissionData;
import sk.styk.martin.apkanalyzer.model.detail.PermissionDataAgregate;
import sk.styk.martin.apkanalyzer.model.detail.UsedPermissionData;

/**
 * Created by Martin Styk on 30.06.2017.
 */
public class PermissionsService {

    public PermissionDataAgregate get(@NonNull PackageInfo packageInfo, @NonNull PackageManager packageManager) {

        List<PermissionData> definedPermissions = getDefinedPermissions(packageInfo, packageManager);
        List<UsedPermissionData> requestedPermissions = getUsedPermissions(packageInfo, packageManager);

        return new PermissionDataAgregate(definedPermissions, requestedPermissions);
    }

    public List<PermissionData> getDefinedPermissions(@NonNull PackageInfo packageInfo, @NonNull PackageManager packageManager) {
        PermissionInfo[] permissionInfos = packageInfo.permissions;

        List<PermissionData> permissionData;

        if (permissionInfos == null || permissionInfos.length == 0) {
            permissionData = new ArrayList<>(0);
        } else {
            permissionData = new ArrayList<>(permissionInfos.length);
            for (PermissionInfo permissionInfo : permissionInfos) {
                permissionData.add(getPermissionData(permissionInfo, packageManager));
            }
        }

        return permissionData;
    }

    private PermissionData getPermissionData(@NonNull PermissionInfo permissionInfo, @NonNull PackageManager packageManager) {

        String description = permissionInfo.loadDescription(packageManager) == null ? "" : permissionInfo.loadDescription(packageManager).toString();

        return new PermissionData(
                permissionInfo.name,
                permissionInfo.group,
                description,
                permissionInfo.protectionLevel,
                null
//                permissionInfo.loadIcon(packageManager)
        );

    }

    public List<UsedPermissionData> getUsedPermissions(@NonNull PackageInfo packageInfo, @NonNull PackageManager packageManager) {
        List<UsedPermissionData> requestedPermissions;

        String[] requestedPermissionNames = packageInfo.requestedPermissions;

        int[] requestedPermissionFlags = new int[0];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            requestedPermissionFlags = packageInfo.requestedPermissionsFlags;
        }

        if (requestedPermissionNames == null) {
            requestedPermissions = new ArrayList<>(0);
        } else {
            requestedPermissions = new ArrayList<>(requestedPermissionNames.length);

            for (int i = 0; i < requestedPermissionNames.length; i++) {
                boolean isGranted = false;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    isGranted = (requestedPermissionFlags[i] & PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0;
                }
                String name = requestedPermissionNames[i];

                PermissionData permissionData = null;
                try {
                    PermissionInfo permissionInfo = packageManager.getPermissionInfo(name, PackageManager.GET_META_DATA);
                    permissionData = getPermissionData(permissionInfo, packageManager);
                } catch (Exception e) {
                    // we failed to get permission data from pacakge manager. Try to use things we know
                    permissionData = new PermissionData(name, null, null, Integer.MIN_VALUE, null);
                }

                requestedPermissions.add(new UsedPermissionData(permissionData, isGranted));
            }
        }

        return requestedPermissions;
    }

}
