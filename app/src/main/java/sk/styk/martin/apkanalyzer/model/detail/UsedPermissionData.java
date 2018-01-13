package sk.styk.martin.apkanalyzer.model.detail;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Martin Styk on 13.01.2018.
 */

public class UsedPermissionData implements Parcelable {

    private PermissionData permissionData;
    private boolean granted;

    public UsedPermissionData(PermissionData permissionData, boolean granted) {
        this.permissionData = permissionData;
        this.granted = granted;
    }

    public PermissionData getPermissionData() {
        return permissionData;
    }

    public boolean isGranted() {
        return granted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsedPermissionData that = (UsedPermissionData) o;

        if (granted != that.granted) return false;
        return permissionData != null ? permissionData.equals(that.permissionData) : that.permissionData == null;
    }

    @Override
    public int hashCode() {
        int result = permissionData != null ? permissionData.hashCode() : 0;
        result = 31 * result + (granted ? 1 : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.permissionData, flags);
        dest.writeByte(this.granted ? (byte) 1 : (byte) 0);
    }

    protected UsedPermissionData(Parcel in) {
        this.permissionData = in.readParcelable(PermissionData.class.getClassLoader());
        this.granted = in.readByte() != 0;
    }

    public static final Parcelable.Creator<UsedPermissionData> CREATOR = new Parcelable.Creator<UsedPermissionData>() {
        @Override
        public UsedPermissionData createFromParcel(Parcel source) {
            return new UsedPermissionData(source);
        }

        @Override
        public UsedPermissionData[] newArray(int size) {
            return new UsedPermissionData[size];
        }
    };
}
