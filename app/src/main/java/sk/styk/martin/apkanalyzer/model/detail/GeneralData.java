package sk.styk.martin.apkanalyzer.model.detail;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Martin Styk on 01.07.2017.
 */

public class GeneralData implements Parcelable {

    private String packageName;
    private String applicationName;
    private String processName;
    private String versionName;
    private int versionCode;
    private boolean isSystemApp;
    private String description;

    private Drawable icon;

    private String apkDirectory;
    private String dataDirectory;
    private String installLocation;

    //bytes
    private long apkSize;

    //todo whole size, data size, cahce size

    //timestamp
    private long firstInstallTime;
    private long lastUpdateTime;

    private int minSdkVersion;
    private String minSdkLabel;

    private int targetSdkVersion;
    private String targetSdkLabel;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public boolean isSystemApp() {
        return isSystemApp;
    }

    public void setSystemApp(boolean systemApp) {
        isSystemApp = systemApp;
    }

    public String getApkDirectory() {
        return apkDirectory;
    }

    public void setApkDirectory(String apkDirectory) {
        this.apkDirectory = apkDirectory;
    }

    public String getDataDirectory() {
        return dataDirectory;
    }

    public void setDataDirectory(String dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    public String getInstallLocation() {
        return installLocation;
    }

    public void setInstallLocation(String installLocation) {
        this.installLocation = installLocation;
    }

    public long getApkSize() {
        return apkSize;
    }

    public void setApkSize(long apkSize) {
        this.apkSize = apkSize;
    }

    public long getFirstInstallTime() {
        return firstInstallTime;
    }

    public void setFirstInstallTime(long firstInstallTime) {
        this.firstInstallTime = firstInstallTime;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public int getMinSdkVersion() {
        return minSdkVersion;
    }

    public void setMinSdkVersion(int minSdkVersion) {
        this.minSdkVersion = minSdkVersion;
    }

    public int getTargetSdkVersion() {
        return targetSdkVersion;
    }

    public void setTargetSdkVersion(int targetSdkVersion) {
        this.targetSdkVersion = targetSdkVersion;
    }

    public String getMinSdkLabel() {
        return minSdkLabel;
    }

    public void setMinSdkLabel(String minSdkLabel) {
        this.minSdkLabel = minSdkLabel;
    }

    public String getTargetSdkLabel() {
        return targetSdkLabel;
    }

    public void setTargetSdkLabel(String targetSdkLabel) {
        this.targetSdkLabel = targetSdkLabel;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeneralData that = (GeneralData) o;

        if (versionCode != that.versionCode) return false;
        if (isSystemApp != that.isSystemApp) return false;
        if (firstInstallTime != that.firstInstallTime) return false;
        if (lastUpdateTime != that.lastUpdateTime) return false;
        if (minSdkVersion != that.minSdkVersion) return false;
        if (targetSdkVersion != that.targetSdkVersion) return false;
        if (apkSize != that.apkSize) return false;
        if (packageName != null ? !packageName.equals(that.packageName) : that.packageName != null)
            return false;
        if (applicationName != null ? !applicationName.equals(that.applicationName) : that.applicationName != null)
            return false;
        if (processName != null ? !processName.equals(that.processName) : that.processName != null)
            return false;
        if (versionName != null ? !versionName.equals(that.versionName) : that.versionName != null)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (icon != null ? !icon.equals(that.icon) : that.icon != null) return false;
        if (apkDirectory != null ? !apkDirectory.equals(that.apkDirectory) : that.apkDirectory != null)
            return false;
        if (dataDirectory != null ? !dataDirectory.equals(that.dataDirectory) : that.dataDirectory != null)
            return false;
        if (installLocation != null ? !installLocation.equals(that.installLocation) : that.installLocation != null)
            return false;
        if (minSdkLabel != null ? !minSdkLabel.equals(that.minSdkLabel) : that.minSdkLabel != null)
            return false;
        return targetSdkLabel != null ? targetSdkLabel.equals(that.targetSdkLabel) : that.targetSdkLabel == null;

    }

    @Override
    public int hashCode() {
        int result = packageName != null ? packageName.hashCode() : 0;
        result = 31 * result + (applicationName != null ? applicationName.hashCode() : 0);
        result = 31 * result + (processName != null ? processName.hashCode() : 0);
        result = 31 * result + (versionName != null ? versionName.hashCode() : 0);
        result = 31 * result + versionCode;
        result = 31 * result + (isSystemApp ? 1 : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        result = 31 * result + (apkDirectory != null ? apkDirectory.hashCode() : 0);
        result = 31 * result + (dataDirectory != null ? dataDirectory.hashCode() : 0);
        result = 31 * result + (installLocation != null ? installLocation.hashCode() : 0);
        result = 31 * result + (int)apkSize;
        result = 31 * result + (int) (firstInstallTime ^ (firstInstallTime >>> 32));
        result = 31 * result + (int) (lastUpdateTime ^ (lastUpdateTime >>> 32));
        result = 31 * result + minSdkVersion;
        result = 31 * result + (minSdkLabel != null ? minSdkLabel.hashCode() : 0);
        result = 31 * result + targetSdkVersion;
        result = 31 * result + (targetSdkLabel != null ? targetSdkLabel.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GeneralData{" +
                "packageName='" + packageName + '\'' +
                ", applicationName='" + applicationName + '\'' +
                ", processName='" + processName + '\'' +
                ", versionName='" + versionName + '\'' +
                ", versionCode=" + versionCode +
                ", isSystemApp=" + isSystemApp +
                ", description='" + description + '\'' +
                ", apkDirectory='" + apkDirectory + '\'' +
                ", dataDirectory='" + dataDirectory + '\'' +
                ", installLocation='" + installLocation + '\'' +
                ", apkSize=" + apkSize +
                ", firstInstallTime=" + firstInstallTime +
                ", lastUpdateTime=" + lastUpdateTime +
                ", minSdkVersion=" + minSdkVersion +
                ", minSdkLabel='" + minSdkLabel + '\'' +
                ", targetSdkVersion=" + targetSdkVersion +
                ", targetSdkLabel='" + targetSdkLabel + '\'' +
                '}';
    }

    public GeneralData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.packageName);
        dest.writeString(this.applicationName);
        dest.writeString(this.processName);
        dest.writeString(this.versionName);
        dest.writeInt(this.versionCode);
        dest.writeByte(this.isSystemApp ? (byte) 1 : (byte) 0);
        dest.writeString(this.description);
        dest.writeString(this.apkDirectory);
        dest.writeString(this.dataDirectory);
        dest.writeString(this.installLocation);
        dest.writeValue(this.apkSize);
        dest.writeLong(this.firstInstallTime);
        dest.writeLong(this.lastUpdateTime);
        dest.writeInt(this.minSdkVersion);
        dest.writeString(this.minSdkLabel);
        dest.writeInt(this.targetSdkVersion);
        dest.writeString(this.targetSdkLabel);
    }

    protected GeneralData(Parcel in) {
        this.packageName = in.readString();
        this.applicationName = in.readString();
        this.processName = in.readString();
        this.versionName = in.readString();
        this.versionCode = in.readInt();
        this.isSystemApp = in.readByte() != 0;
        this.description = in.readString();
        this.apkDirectory = in.readString();
        this.dataDirectory = in.readString();
        this.installLocation = in.readString();
        this.apkSize = (Long) in.readValue(Long.class.getClassLoader());
        this.firstInstallTime = in.readLong();
        this.lastUpdateTime = in.readLong();
        this.minSdkVersion = in.readInt();
        this.minSdkLabel = in.readString();
        this.targetSdkVersion = in.readInt();
        this.targetSdkLabel = in.readString();
    }

    public static final Creator<GeneralData> CREATOR = new Creator<GeneralData>() {
        @Override
        public GeneralData createFromParcel(Parcel source) {
            return new GeneralData(source);
        }

        @Override
        public GeneralData[] newArray(int size) {
            return new GeneralData[size];
        }
    };
}
