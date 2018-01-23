package sk.styk.martin.apkanalyzer.model.list;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import sk.styk.martin.apkanalyzer.business.service.GeneralDataService;
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData;
import sk.styk.martin.apkanalyzer.model.detail.AppSource;

/**
 * Class holding basic application metadata used in list view of all apps
 * For more detailed application metadata see {@link AppDetailData}
 * <p>
 *
 * @author Martin Styk
 * @version 15.06.2017.
 */
public class AppListData implements Parcelable {

    private String packageName;
    private String applicationName;
    private int version;
    private boolean isSystemApp;
    private AppSource source;
    private Drawable icon;

    private PackageManager packageManager;

    private ApplicationInfo applicationInfo;

    public AppListData(PackageInfo packageInfo, PackageManager packageManager) {
        this.packageManager = packageManager;
        this.applicationInfo = packageInfo.applicationInfo;
        this.version = packageInfo.versionCode;
        this.isSystemApp = (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
        this.source = GeneralDataService.Companion.getAppSource(packageManager, getPackageName(), isSystemApp);
    }

    public String getApplicationName() {
        if (applicationName == null) {
            CharSequence label = applicationInfo.loadLabel(packageManager);
            applicationName = label != null ? label.toString() : applicationInfo.packageName;
        }
        return applicationName;
    }

    public String getPackageName() {
        return applicationInfo.packageName;
    }

    public int getVersion() {
        return version;
    }

    public AppSource getSource() {
        return source;
    }

    public Drawable getIcon() {
        if (icon == null) {
            icon = applicationInfo.loadIcon(packageManager);
        }
        return icon;
    }

    public boolean isSystemApp() {
        return isSystemApp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppListData that = (AppListData) o;

        if (version != that.version) return false;
        if (isSystemApp != that.isSystemApp) return false;
        if (packageName != null ? !packageName.equals(that.packageName) : that.packageName != null)
            return false;
        if (applicationName != null ? !applicationName.equals(that.applicationName) : that.applicationName != null)
            return false;
        if (source != that.source) return false;
        if (packageManager != null ? !packageManager.equals(that.packageManager) : that.packageManager != null)
            return false;
        return applicationInfo != null ? applicationInfo.equals(that.applicationInfo) : that.applicationInfo == null;
    }

    @Override
    public int hashCode() {
        int result = packageName != null ? packageName.hashCode() : 0;
        result = 31 * result + (applicationName != null ? applicationName.hashCode() : 0);
        result = 31 * result + version;
        result = 31 * result + (isSystemApp ? 1 : 0);
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (packageManager != null ? packageManager.hashCode() : 0);
        result = 31 * result + (applicationInfo != null ? applicationInfo.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return applicationName + " " + packageName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.packageName);
        dest.writeString(this.applicationName);
        dest.writeInt(this.version);
    }

    public AppListData() {
    }

    protected AppListData(Parcel in) {
        this.packageName = in.readString();
        this.applicationName = in.readString();
        this.version = in.readInt();
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
