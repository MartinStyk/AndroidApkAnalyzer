package sk.styk.martin.apkanalyzer.model.detail;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Martin Styk
 * @version 22.06.2017.
 */
public class ActivityData implements Parcelable {

    private String name;
    private String packageName;
    private String label;

    // If this is an activity alias, this is the real activity class to run for it.
    private String targetActivity;

    //Optional name of a permission required to be able to access this Activity
    private String permission;

    // If defined, the activity named here is the logical parent of this activity
    private String parentName;

    /**
     * Set to true if this component is available for use by other applications.
     */
    private boolean exported;

    public ActivityData(String name, String packageName, String label, String targetActivity, String permission, String parentName, boolean exported) {
        this.name = name;
        this.packageName = packageName;
        this.label = label;
        this.targetActivity = targetActivity;
        this.permission = permission;
        this.parentName = parentName;
        this.exported = exported;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public String getTargetActivity() {
        return targetActivity;
    }

    public String getPermission() {
        return permission;
    }

    public String getParentName() {
        return parentName;
    }

    public boolean isExported() {
        return exported;
    }

    public String getPackageName() {
        return packageName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActivityData that = (ActivityData) o;

        if (exported != that.exported) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (packageName != null ? !packageName.equals(that.packageName) : that.packageName != null)
            return false;
        if (label != null ? !label.equals(that.label) : that.label != null) return false;
        if (targetActivity != null ? !targetActivity.equals(that.targetActivity) : that.targetActivity != null)
            return false;
        if (permission != null ? !permission.equals(that.permission) : that.permission != null)
            return false;
        return parentName != null ? parentName.equals(that.parentName) : that.parentName == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (packageName != null ? packageName.hashCode() : 0);
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (targetActivity != null ? targetActivity.hashCode() : 0);
        result = 31 * result + (permission != null ? permission.hashCode() : 0);
        result = 31 * result + (parentName != null ? parentName.hashCode() : 0);
        result = 31 * result + (exported ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ActivityData{" +
                "name='" + name + '\'' +
                ", packageName='" + packageName + '\'' +
                ", label='" + label + '\'' +
                ", targetActivity='" + targetActivity + '\'' +
                ", permission='" + permission + '\'' +
                ", parentName='" + parentName + '\'' +
                ", exported=" + exported +
                '}';
    }

    public ActivityData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.packageName);
        dest.writeString(this.label);
        dest.writeString(this.targetActivity);
        dest.writeString(this.permission);
        dest.writeString(this.parentName);
        dest.writeByte(this.exported ? (byte) 1 : (byte) 0);
    }

    protected ActivityData(Parcel in) {
        this.name = in.readString();
        this.packageName = in.readString();
        this.label = in.readString();
        this.targetActivity = in.readString();
        this.permission = in.readString();
        this.parentName = in.readString();
        this.exported = in.readByte() != 0;
    }

    public static final Creator<ActivityData> CREATOR = new Creator<ActivityData>() {
        @Override
        public ActivityData createFromParcel(Parcel source) {
            return new ActivityData(source);
        }

        @Override
        public ActivityData[] newArray(int size) {
            return new ActivityData[size];
        }
    };
}

