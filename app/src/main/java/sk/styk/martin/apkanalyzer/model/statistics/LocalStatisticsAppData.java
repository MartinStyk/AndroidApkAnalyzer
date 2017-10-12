package sk.styk.martin.apkanalyzer.model.statistics;

import android.os.Parcel;
import android.os.Parcelable;

import sk.styk.martin.apkanalyzer.model.detail.AppSource;

/**
 * Represents single app data for statistics computation.
 * <p>
 * Created by Martin Styk on 28.07.2017.
 */
public class LocalStatisticsAppData implements Parcelable {

    private boolean systemApp;
    private int installLocation;
    private int targetSdk;
    private int minSdk;
    private long apkSize;

    private AppSource appSource;

    private String signAlgorithm;

    private int activities;
    private int services;
    private int providers;
    private int receivers;

    private int usedPermissions;
    private int definedPermissions;

    private int files;

    private int drawables;
    private int differentDrawables;

    private int layouts;
    private int differentLayouts;

    public boolean isSystemApp() {
        return systemApp;
    }

    public void setSystemApp(boolean systemApp) {
        this.systemApp = systemApp;
    }

    public int getInstallLocation() {
        return installLocation;
    }

    public void setInstallLocation(int installLocation) {
        this.installLocation = installLocation;
    }

    public int getTargetSdk() {
        return targetSdk;
    }

    public void setTargetSdk(int targetSdk) {
        this.targetSdk = targetSdk;
    }

    public int getMinSdk() {
        return minSdk;
    }

    public void setMinSdk(int minSdk) {
        this.minSdk = minSdk;
    }

    public AppSource getAppSource() {
        return appSource;
    }

    public void setAppSource(AppSource appSource) {
        this.appSource = appSource;
    }

    public long getApkSize() {
        return apkSize;
    }

    public void setApkSize(long apkSize) {
        this.apkSize = apkSize;
    }

    public String getSignAlgorithm() {
        return signAlgorithm;
    }

    public void setSignAlgorithm(String signAlgorithm) {
        this.signAlgorithm = signAlgorithm;
    }

    public int getActivities() {
        return activities;
    }

    public void setActivities(int activities) {
        this.activities = activities;
    }

    public int getServices() {
        return services;
    }

    public void setServices(int services) {
        this.services = services;
    }

    public int getProviders() {
        return providers;
    }

    public void setProviders(int providers) {
        this.providers = providers;
    }

    public int getReceivers() {
        return receivers;
    }

    public void setReceivers(int receivers) {
        this.receivers = receivers;
    }

    public int getUsedPermissions() {
        return usedPermissions;
    }

    public void setUsedPermissions(int usedPermissions) {
        this.usedPermissions = usedPermissions;
    }

    public int getDefinedPermissions() {
        return definedPermissions;
    }

    public void setDefinedPermissions(int definedPermissions) {
        this.definedPermissions = definedPermissions;
    }

    public int getFiles() {
        return files;
    }

    public void setFiles(int files) {
        this.files = files;
    }

    public int getDrawables() {
        return drawables;
    }

    public void setDrawables(int drawables) {
        this.drawables = drawables;
    }

    public int getDifferentDrawables() {
        return differentDrawables;
    }

    public void setDifferentDrawables(int differentDrawables) {
        this.differentDrawables = differentDrawables;
    }

    public int getLayouts() {
        return layouts;
    }

    public void setLayouts(int layouts) {
        this.layouts = layouts;
    }

    public int getDifferentLayouts() {
        return differentLayouts;
    }

    public void setDifferentLayouts(int differentLayouts) {
        this.differentLayouts = differentLayouts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocalStatisticsAppData data = (LocalStatisticsAppData) o;

        if (systemApp != data.systemApp) return false;
        if (installLocation != data.installLocation) return false;
        if (targetSdk != data.targetSdk) return false;
        if (minSdk != data.minSdk) return false;
        if (apkSize != data.apkSize) return false;
        if (activities != data.activities) return false;
        if (services != data.services) return false;
        if (providers != data.providers) return false;
        if (receivers != data.receivers) return false;
        if (usedPermissions != data.usedPermissions) return false;
        if (definedPermissions != data.definedPermissions) return false;
        if (files != data.files) return false;
        if (drawables != data.drawables) return false;
        if (differentDrawables != data.differentDrawables) return false;
        if (layouts != data.layouts) return false;
        if (differentLayouts != data.differentLayouts) return false;
        if (appSource != data.appSource) return false;
        return signAlgorithm != null ? signAlgorithm.equals(data.signAlgorithm) : data.signAlgorithm == null;

    }

    @Override
    public int hashCode() {
        int result = (systemApp ? 1 : 0);
        result = 31 * result + installLocation;
        result = 31 * result + targetSdk;
        result = 31 * result + minSdk;
        result = 31 * result + (int) (apkSize ^ (apkSize >>> 32));
        result = 31 * result + (appSource != null ? appSource.hashCode() : 0);
        result = 31 * result + (signAlgorithm != null ? signAlgorithm.hashCode() : 0);
        result = 31 * result + activities;
        result = 31 * result + services;
        result = 31 * result + providers;
        result = 31 * result + receivers;
        result = 31 * result + usedPermissions;
        result = 31 * result + definedPermissions;
        result = 31 * result + files;
        result = 31 * result + drawables;
        result = 31 * result + differentDrawables;
        result = 31 * result + layouts;
        result = 31 * result + differentLayouts;
        return result;
    }

    public LocalStatisticsAppData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.systemApp ? (byte) 1 : (byte) 0);
        dest.writeInt(this.installLocation);
        dest.writeInt(this.targetSdk);
        dest.writeInt(this.minSdk);
        dest.writeLong(this.apkSize);
        dest.writeInt(this.appSource == null ? -1 : this.appSource.ordinal());
        dest.writeString(this.signAlgorithm);
        dest.writeInt(this.activities);
        dest.writeInt(this.services);
        dest.writeInt(this.providers);
        dest.writeInt(this.receivers);
        dest.writeInt(this.usedPermissions);
        dest.writeInt(this.definedPermissions);
        dest.writeInt(this.files);
        dest.writeInt(this.drawables);
        dest.writeInt(this.differentDrawables);
        dest.writeInt(this.layouts);
        dest.writeInt(this.differentLayouts);
    }

    protected LocalStatisticsAppData(Parcel in) {
        this.systemApp = in.readByte() != 0;
        this.installLocation = in.readInt();
        this.targetSdk = in.readInt();
        this.minSdk = in.readInt();
        this.apkSize = in.readLong();
        int tmpAppSource = in.readInt();
        this.appSource = tmpAppSource == -1 ? null : AppSource.values()[tmpAppSource];
        this.signAlgorithm = in.readString();
        this.activities = in.readInt();
        this.services = in.readInt();
        this.providers = in.readInt();
        this.receivers = in.readInt();
        this.usedPermissions = in.readInt();
        this.definedPermissions = in.readInt();
        this.files = in.readInt();
        this.drawables = in.readInt();
        this.differentDrawables = in.readInt();
        this.layouts = in.readInt();
        this.differentLayouts = in.readInt();
    }

    public static final Creator<LocalStatisticsAppData> CREATOR = new Creator<LocalStatisticsAppData>() {
        @Override
        public LocalStatisticsAppData createFromParcel(Parcel source) {
            return new LocalStatisticsAppData(source);
        }

        @Override
        public LocalStatisticsAppData[] newArray(int size) {
            return new LocalStatisticsAppData[size];
        }
    };
}
