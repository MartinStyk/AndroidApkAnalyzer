package sk.styk.martin.apkanalyzer.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Martin Styk on 22.06.2017.
 */
public class PermissionData implements Parcelable {

    private List<String> definesPermissions;

    private List<String> usesPermissions;

    public List<String> getDefinesPermissions() {
        return definesPermissions;
    }

    public void setDefinesPermissions(List<String> definesPermissions) {
        this.definesPermissions = definesPermissions;
    }

    public List<String> getUsesPermissions() {
        return usesPermissions;
    }

    public void setUsesPermissions(List<String> usesPermissions) {
        this.usesPermissions = usesPermissions;
    }

    @Override
    public String toString() {
        return "PermissionData{" +
                "definesPermissions=" + definesPermissions +
                ", usesPermissions=" + usesPermissions +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PermissionData that = (PermissionData) o;

        if (definesPermissions != null ? !definesPermissions.equals(that.definesPermissions) : that.definesPermissions != null)
            return false;
        return usesPermissions != null ? usesPermissions.equals(that.usesPermissions) : that.usesPermissions == null;

    }

    @Override
    public int hashCode() {
        int result = definesPermissions != null ? definesPermissions.hashCode() : 0;
        result = 31 * result + (usesPermissions != null ? usesPermissions.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.definesPermissions);
        dest.writeStringList(this.usesPermissions);
    }

    public PermissionData() {
    }

    protected PermissionData(Parcel in) {
        this.definesPermissions = in.createStringArrayList();
        this.usesPermissions = in.createStringArrayList();
    }

    public static final Creator<PermissionData> CREATOR = new Creator<PermissionData>() {
        @Override
        public PermissionData createFromParcel(Parcel source) {
            return new PermissionData(source);
        }

        @Override
        public PermissionData[] newArray(int size) {
            return new PermissionData[size];
        }
    };
}