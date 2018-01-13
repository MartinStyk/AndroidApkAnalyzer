package sk.styk.martin.apkanalyzer.model.permissions;

import java.util.List;

import sk.styk.martin.apkanalyzer.model.detail.PermissionData;

/**
 * Created by Martin Styk on 13.01.2018.
 */
public class LocalPermissionData {

    private PermissionData permissionData;
    private List<PermissionStatus> permissionStatusList;

    public LocalPermissionData( PermissionData permissionData, List<PermissionStatus> permissionStatusList) {
        this.permissionData = permissionData;
        this.permissionStatusList = permissionStatusList;
    }

    public PermissionData getPermissionData() {
        return permissionData;
    }

    public List<PermissionStatus> getPermissionStatusList() {
        return permissionStatusList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocalPermissionData that = (LocalPermissionData) o;

        if (permissionData != null ? !permissionData.equals(that.permissionData) : that.permissionData != null)
            return false;
        return permissionStatusList != null ? permissionStatusList.equals(that.permissionStatusList) : that.permissionStatusList == null;
    }

    @Override
    public int hashCode() {
        int result = permissionData != null ? permissionData.hashCode() : 0;
        result = 31 * result + (permissionStatusList != null ? permissionStatusList.hashCode() : 0);
        return result;
    }
}

