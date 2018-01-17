package sk.styk.martin.apkanalyzer.model.permissions;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import sk.styk.martin.apkanalyzer.model.detail.PermissionData;

/**
 * Created by Martin Styk on 13.01.2018.
 */
public class LocalPermissionData implements Parcelable {

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

    public List<String> getGrantedPackageNames(){
        List<String> packageNames = new ArrayList<>();
        for (PermissionStatus permissionStatus : permissionStatusList) {
            if(permissionStatus.isGranted())
                packageNames.add(permissionStatus.getPackageName());
        }
        return packageNames;
    }


    public List<String> getNotGrantedPackageNames(){
        List<String> packageNames = new ArrayList<>();
        for (PermissionStatus permissionStatus : permissionStatusList) {
            if(!permissionStatus.isGranted())
                packageNames.add(permissionStatus.getPackageName());
        }
        return packageNames;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.permissionData, flags);
        dest.writeTypedList(this.permissionStatusList);
    }

    protected LocalPermissionData(Parcel in) {
        this.permissionData = in.readParcelable(PermissionData.class.getClassLoader());
        this.permissionStatusList = in.createTypedArrayList(PermissionStatus.CREATOR);
    }

    public static final Creator<LocalPermissionData> CREATOR = new Creator<LocalPermissionData>() {
        @Override
        public LocalPermissionData createFromParcel(Parcel source) {
            return new LocalPermissionData(source);
        }

        @Override
        public LocalPermissionData[] newArray(int size) {
            return new LocalPermissionData[size];
        }
    };
}

