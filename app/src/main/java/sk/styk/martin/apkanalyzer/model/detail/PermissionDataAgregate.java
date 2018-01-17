package sk.styk.martin.apkanalyzer.model.detail;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Styk
 * @version 22.06.2017.
 */
public class PermissionDataAgregate implements Parcelable {

    private List<PermissionData> definesPermissions;
    private List<UsedPermissionData> usesPermissions;

    public PermissionDataAgregate(List<PermissionData> definesPermissions,
                                  List<UsedPermissionData> usesPermissions) {
        this.definesPermissions = definesPermissions;
        this.usesPermissions = usesPermissions;
    }

    public List<PermissionData> getDefinesPermissions() {
        return definesPermissions;
    }

    public List<UsedPermissionData> getUsesPermissions() {
        return usesPermissions;
    }

    public List<String> getUsesPermissionsNames() {
        if (usesPermissions == null)
            return new ArrayList<>(0);

        List<String> names = new ArrayList<>(usesPermissions.size());
        for (UsedPermissionData data : usesPermissions)
            names.add(data.getPermissionData().getName());
        return names;
    }

    public List<String> getDefinesPermissionsNames() {
        if (definesPermissions == null)
            return new ArrayList<>(0);

        List<String> names = new ArrayList<>(definesPermissions.size());
        for (PermissionData data : definesPermissions)
            names.add(data.getName());
        return names;
    }

    @Override
    public String toString() {
        return "LocalPermissionData{" +
                "definesPermissions=" + definesPermissions +
                ", usesPermissions=" + usesPermissions +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PermissionDataAgregate that = (PermissionDataAgregate) o;

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
        dest.writeTypedList(this.definesPermissions);
        dest.writeTypedList(this.usesPermissions);
    }

    protected PermissionDataAgregate(Parcel in) {
        this.definesPermissions = in.createTypedArrayList(PermissionData.CREATOR);
        this.usesPermissions = in.createTypedArrayList(UsedPermissionData.CREATOR);
    }

    public static final Creator<PermissionDataAgregate> CREATOR = new Creator<PermissionDataAgregate>() {
        @Override
        public PermissionDataAgregate createFromParcel(Parcel source) {
            return new PermissionDataAgregate(source);
        }

        @Override
        public PermissionDataAgregate[] newArray(int size) {
            return new PermissionDataAgregate[size];
        }
    };
}