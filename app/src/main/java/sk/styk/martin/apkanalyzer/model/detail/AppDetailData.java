package sk.styk.martin.apkanalyzer.model.detail;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Class holding basic application metadata used in application detail view
 * <p>
 * @author Martin Styk
 * @version 22.06.2017.
 */
public class AppDetailData implements Parcelable {

    private AnalysisMode analysisMode;
    private GeneralData generalData;
    private CertificateData certificateData;
    private List<ActivityData> activityData;
    private List<ServiceData> serviceData;
    private List<ContentProviderData> contentProviderData;
    private List<BroadcastReceiverData> broadcastReceiverData;
    private PermissionDataAgregate permissionData;
    private List<FeatureData> featureData;
    private FileData fileData;
    private ResourceData resourceData;
    private ClassPathData classPathData;

    public AppDetailData(AnalysisMode analysisMode) {
        this.analysisMode = analysisMode;
    }

    public AnalysisMode getAnalysisMode() {
        return analysisMode;
    }

    public void setAnalysisMode(AnalysisMode analysisMode) {
        this.analysisMode = analysisMode;
    }

    public boolean isAnalyzedApkFile(){
        return AnalysisMode.APK_FILE.equals(analysisMode);
    }

    public boolean isAnalyzedInstalledPackage(){
        return AnalysisMode.INSTALLED_PACKAGE.equals(analysisMode);
    }

    public GeneralData getGeneralData() {
        return generalData;
    }

    public void setGeneralData(GeneralData generalData) {
        this.generalData = generalData;
    }

    public CertificateData getCertificateData() {
        return certificateData;
    }

    public void setCertificateData(CertificateData certificateData) {
        this.certificateData = certificateData;
    }

    public List<ActivityData> getActivityData() {
        return activityData;
    }

    public void setActivityData(List<ActivityData> activityData) {
        this.activityData = activityData;
    }

    public List<ServiceData> getServiceData() {
        return serviceData;
    }

    public void setServiceData(List<ServiceData> serviceData) {
        this.serviceData = serviceData;
    }

    public List<ContentProviderData> getContentProviderData() {
        return contentProviderData;
    }

    public void setContentProviderData(List<ContentProviderData> contentProviderData) {
        this.contentProviderData = contentProviderData;
    }

    public List<BroadcastReceiverData> getBroadcastReceiverData() {
        return broadcastReceiverData;
    }

    public void setBroadcastReceiverData(List<BroadcastReceiverData> broadcastReceiverData) {
        this.broadcastReceiverData = broadcastReceiverData;
    }

    public PermissionDataAgregate getPermissionData() {
        return permissionData;
    }

    public void setPermissionData(PermissionDataAgregate permissionData) {
        this.permissionData = permissionData;
    }

    public FileData getFileData() {
        return fileData;
    }

    public void setFileData(FileData fileData) {
        this.fileData = fileData;
    }

    public ResourceData getResourceData() {
        return resourceData;
    }

    public void setResourceData(ResourceData resourceData) {
        this.resourceData = resourceData;
    }

    public List<FeatureData> getFeatureData() {
        return featureData;
    }

    public void setFeatureData(List<FeatureData> featureData) {
        this.featureData = featureData;
    }

    public ClassPathData getClassPathData() {
        return classPathData;
    }

    public void setClassPathData(ClassPathData classPathData) {
        this.classPathData = classPathData;
    }

    @Override
    public String toString() {
        return "AppDetailData{" +
                "analysisMode=" + analysisMode +
                ", generalData=" + generalData +
                ", certificateData=" + certificateData +
                ", activityData=" + activityData +
                ", serviceData=" + serviceData +
                ", contentProviderData=" + contentProviderData +
                ", broadcastReceiverData=" + broadcastReceiverData +
                ", permissionData=" + permissionData +
                ", featureData=" + featureData +
                ", fileData=" + fileData +
                ", resourceData=" + resourceData +
                ", classes=" + classPathData +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppDetailData data = (AppDetailData) o;

        if (analysisMode != data.analysisMode) return false;
        if (generalData != null ? !generalData.equals(data.generalData) : data.generalData != null)
            return false;
        if (certificateData != null ? !certificateData.equals(data.certificateData) : data.certificateData != null)
            return false;
        if (activityData != null ? !activityData.equals(data.activityData) : data.activityData != null)
            return false;
        if (serviceData != null ? !serviceData.equals(data.serviceData) : data.serviceData != null)
            return false;
        if (contentProviderData != null ? !contentProviderData.equals(data.contentProviderData) : data.contentProviderData != null)
            return false;
        if (broadcastReceiverData != null ? !broadcastReceiverData.equals(data.broadcastReceiverData) : data.broadcastReceiverData != null)
            return false;
        if (permissionData != null ? !permissionData.equals(data.permissionData) : data.permissionData != null)
            return false;
        if (featureData != null ? !featureData.equals(data.featureData) : data.featureData != null)
            return false;
        if (fileData != null ? !fileData.equals(data.fileData) : data.fileData != null)
            return false;
        if (resourceData != null ? !resourceData.equals(data.resourceData) : data.resourceData != null)
            return false;
        return classPathData != null ? classPathData.equals(data.classPathData) : data.classPathData == null;

    }

    @Override
    public int hashCode() {
        int result = analysisMode != null ? analysisMode.hashCode() : 0;
        result = 31 * result + (generalData != null ? generalData.hashCode() : 0);
        result = 31 * result + (certificateData != null ? certificateData.hashCode() : 0);
        result = 31 * result + (activityData != null ? activityData.hashCode() : 0);
        result = 31 * result + (serviceData != null ? serviceData.hashCode() : 0);
        result = 31 * result + (contentProviderData != null ? contentProviderData.hashCode() : 0);
        result = 31 * result + (broadcastReceiverData != null ? broadcastReceiverData.hashCode() : 0);
        result = 31 * result + (permissionData != null ? permissionData.hashCode() : 0);
        result = 31 * result + (featureData != null ? featureData.hashCode() : 0);
        result = 31 * result + (fileData != null ? fileData.hashCode() : 0);
        result = 31 * result + (resourceData != null ? resourceData.hashCode() : 0);
        result = 31 * result + (classPathData != null ? classPathData.hashCode() : 0);
        return result;
    }

    public enum AnalysisMode {
        INSTALLED_PACKAGE,
        APK_FILE
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.analysisMode == null ? -1 : this.analysisMode.ordinal());
        dest.writeParcelable(this.generalData, flags);
        dest.writeParcelable(this.certificateData, flags);
        dest.writeTypedList(this.activityData);
        dest.writeTypedList(this.serviceData);
        dest.writeTypedList(this.contentProviderData);
        dest.writeTypedList(this.broadcastReceiverData);
        dest.writeParcelable(this.permissionData, flags);
        dest.writeTypedList(this.featureData);
        dest.writeParcelable(this.fileData, flags);
        dest.writeParcelable(this.resourceData, flags);
        dest.writeParcelable(this.classPathData,flags);
    }

    protected AppDetailData(Parcel in) {
        int tmpAnalysisMode = in.readInt();
        this.analysisMode = tmpAnalysisMode == -1 ? null : AnalysisMode.values()[tmpAnalysisMode];
        this.generalData = in.readParcelable(GeneralData.class.getClassLoader());
        this.certificateData = in.readParcelable(CertificateData.class.getClassLoader());
        this.activityData = in.createTypedArrayList(ActivityData.CREATOR);
        this.serviceData = in.createTypedArrayList(ServiceData.CREATOR);
        this.contentProviderData = in.createTypedArrayList(ContentProviderData.CREATOR);
        this.broadcastReceiverData = in.createTypedArrayList(BroadcastReceiverData.CREATOR);
        this.permissionData = in.readParcelable(PermissionDataAgregate.class.getClassLoader());
        this.featureData = in.createTypedArrayList(FeatureData.CREATOR);
        this.fileData = in.readParcelable(FileData.class.getClassLoader());
        this.resourceData = in.readParcelable(ResourceData.class.getClassLoader());
        this.classPathData = in.readParcelable(ClassPathData.class.getClassLoader());
    }

    public static final Creator<AppDetailData> CREATOR = new Creator<AppDetailData>() {
        @Override
        public AppDetailData createFromParcel(Parcel source) {
            return new AppDetailData(source);
        }

        @Override
        public AppDetailData[] newArray(int size) {
            return new AppDetailData[size];
        }
    };
}
