package sk.styk.martin.apkanalyzer.business.service;

import android.content.pm.PackageInfo;
import android.content.pm.PermissionInfo;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sk.styk.martin.apkanalyzer.model.detail.PermissionData;

/**
 * Created by Martin Styk on 30.06.2017.
 */
public class PermissionsService {

    public PermissionData get(@NonNull PackageInfo packageInfo) {

        PermissionInfo[] permissionInfos = packageInfo.permissions;
        List<String> definedPermissions;

        if (permissionInfos == null || permissionInfos.length == 0) {
            definedPermissions = new ArrayList<>(0);
        } else {
            definedPermissions = new ArrayList<>(permissionInfos.length);
            for (PermissionInfo permissionInfo : permissionInfos) {
                definedPermissions.add(permissionInfo.name);
            }
        }

        String[] requestedPermissionInfos = packageInfo.requestedPermissions;
        List<String> requestedPermissions;

        if (requestedPermissionInfos == null) {
            requestedPermissions = new ArrayList<>(0);
        } else {
            requestedPermissions = new ArrayList<>(requestedPermissionInfos.length);
            Collections.addAll(requestedPermissions, requestedPermissionInfos);
        }

        PermissionData myData = new PermissionData();
        myData.setDefinesPermissions(definedPermissions);
        myData.setUsesPermissions(requestedPermissions);

        return myData;
    }

}
