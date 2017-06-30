package sk.styk.martin.apkanalyzer.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Martin Styk on 22.06.2017.
 */
public class ActivityData implements Parcelable {

    private String name;
    private String label;

    // If this is an activity alias, this is the real activity class to run for it.
    private String targetActivity;

    //Optional name of a permission required to be able to access this Activity
    private String permission;

    // If defined, the activity named here is the logical parent of this activity
    private String parentName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTargetActivity() {
        return targetActivity;
    }

    public void setTargetActivity(String targetActivity) {
        this.targetActivity = targetActivity;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActivityData that = (ActivityData) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (label != null ? !label.equals(that.label) : that.label != null) return false;
        if (targetActivity != null ? !targetActivity.equals(that.targetActivity) : that.targetActivity != null)
            return false;
        if (permission != null ? !permission.equals(that.permission) : that.permission != null)
            return false;
        return parentName != null ? parentName.equals(that.parentName) : that.parentName == null;

    }

    @Override
    public String toString() {
        return "ActivityData{" +
                "name='" + name + '\'' +
                ", label='" + label + '\'' +
                ", targetActivity='" + targetActivity + '\'' +
                ", permission='" + permission + '\'' +
                ", parentName='" + parentName + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (targetActivity != null ? targetActivity.hashCode() : 0);
        result = 31 * result + (permission != null ? permission.hashCode() : 0);
        result = 31 * result + (parentName != null ? parentName.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.label);
        dest.writeString(this.targetActivity);
        dest.writeString(this.permission);
        dest.writeString(this.parentName);
    }

    public ActivityData() {
    }

    protected ActivityData(Parcel in) {
        this.name = in.readString();
        this.label = in.readString();
        this.targetActivity = in.readString();
        this.permission = in.readString();
        this.parentName = in.readString();
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

