package sk.styk.martin.apkanalyzer.model;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

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

    private String pathToApk;

    private boolean isSystemApp;

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

    public String getPathToApk() {
        return pathToApk;
    }

    public void setPathToApk(String pathToApk) {
        this.pathToApk = pathToApk;
    }

    public boolean isSystemApp() {
        return isSystemApp;
    }

    public void setSystemApp(boolean systemApp) {
        isSystemApp = systemApp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppListData that = (AppListData) o;

        if (isSystemApp != that.isSystemApp) return false;
        if (packageName != null ? !packageName.equals(that.packageName) : that.packageName != null)
            return false;
        if (applicationName != null ? !applicationName.equals(that.applicationName) : that.applicationName != null)
            return false;
        if (icon != null ? !icon.equals(that.icon) : that.icon != null) return false;
        return pathToApk != null ? pathToApk.equals(that.pathToApk) : that.pathToApk == null;

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
        result = 31 * result + (pathToApk != null ? pathToApk.hashCode() : 0);
        result = 31 * result + (isSystemApp ? 1 : 0);
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
        dest.writeString(this.pathToApk);
        dest.writeByte(this.isSystemApp ? (byte) 1 : (byte) 0);
    }

    public AppListData() {
    }

    protected AppListData(Parcel in) {
        this.packageName = in.readString();
        this.applicationName = in.readString();
        this.pathToApk = in.readString();
        this.isSystemApp = in.readByte() != 0;
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
