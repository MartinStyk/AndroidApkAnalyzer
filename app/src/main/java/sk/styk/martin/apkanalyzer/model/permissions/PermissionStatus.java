package sk.styk.martin.apkanalyzer.model.permissions;

import android.os.Parcel;
import android.os.Parcelable;

import sk.styk.martin.apkanalyzer.business.service.PermissionsService;

/**
 * Created by Martin Styk on 13.01.2018.
 */
public class PermissionStatus implements Parcelable {
    private String packageName;
    private boolean isGranted;

    public PermissionStatus(String packageName, boolean isGranted){
        this.packageName = packageName;
        this.isGranted = isGranted;
    }

    public String getPackageName() {
        return packageName;
    }

    public boolean isGranted() {
        return isGranted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PermissionStatus that = (PermissionStatus) o;

        if (isGranted != that.isGranted) return false;
        return packageName != null ? packageName.equals(that.packageName) : that.packageName == null;
    }

    @Override
    public int hashCode() {
        int result = packageName != null ? packageName.hashCode() : 0;
        result = 31 * result + (isGranted ? 1 : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.packageName);
        dest.writeByte(this.isGranted ? (byte) 1 : (byte) 0);
    }

    protected PermissionStatus(Parcel in) {
        this.packageName = in.readString();
        this.isGranted = in.readByte() != 0;
    }

    public static final Creator<PermissionStatus> CREATOR = new Creator<PermissionStatus>() {
        @Override
        public PermissionStatus createFromParcel(Parcel source) {
            return new PermissionStatus(source);
        }

        @Override
        public PermissionStatus[] newArray(int size) {
            return new PermissionStatus[size];
        }
    };
}
