package sk.styk.martin.apkanalyzer.model.list;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import sk.styk.martin.apkanalyzer.model.detail.AppDetailData;

/**
 * Class holding basic application metadata used in list view of all apps
 * For more detailed application metadata see {@link AppDetailData}
 *
 * Created by Martin Styk on 15.06.2017.
 */
public class AppListData implements Parcelable {

    private String packageName;

    private String applicationName;

    private Drawable icon;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppListData listData = (AppListData) o;

        if (packageName != null ? !packageName.equals(listData.packageName) : listData.packageName != null)
            return false;
        if (applicationName != null ? !applicationName.equals(listData.applicationName) : listData.applicationName != null)
            return false;
        return icon != null ? icon.equals(listData.icon) : listData.icon == null;

    }

    @Override
    public String toString() {
        return applicationName + " " + packageName;
    }

    @Override
    public int hashCode() {
        int result = packageName != null ? packageName.hashCode() : 0;
        result = 31 * result + (applicationName != null ? applicationName.hashCode() : 0);
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.packageName);
        dest.writeString(this.applicationName);
    }

    public AppListData() {
    }

    protected AppListData(Parcel in) {
        this.packageName = in.readString();
        this.applicationName = in.readString();
    }

    public static final Parcelable.Creator<AppListData> CREATOR = new Parcelable.Creator<AppListData>() {
        @Override
        public AppListData createFromParcel(Parcel source) {
            return new AppListData(source);
        }

        @Override
        public AppListData[] newArray(int size) {
            return new AppListData[size];
        }
    };
}
