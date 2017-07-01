package sk.styk.martin.apkanalyzer.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Class holding basic application metadata used application detail view
 *
 * Created by Martin Styk on 22.06.2017.
 */

public class AppDetailData implements Parcelable {

    private GeneralData generalData;

    private CertificateData certificateData;

    private List<ActivityData> activityData;

    private List<ServiceData> serviceData;

    private List<ContentProviderData> contentProviderData;

    private List<BroadcastReceiverData> broadcastReceiverData;

    private PermissionData permissionData;

    public AppDetailData(){
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

    public PermissionData getPermissionData() {
        return permissionData;
    }

    public void setPermissionData(PermissionData permissionData) {
        this.permissionData = permissionData;
    }

    @Override
    public String toString() {
        return "AppDetailData{" +
                ", generalData=" + generalData +
                ", certificateData=" + certificateData +
                ", activityData=" + activityData +
                ", serviceData=" + serviceData +
                ", contentProviderData=" + contentProviderData +
                ", broadcastReceiverData=" + broadcastReceiverData +
                ", permissionData=" + permissionData +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.generalData, flags);
        dest.writeParcelable(this.certificateData, flags);
        dest.writeTypedList(this.activityData);
        dest.writeTypedList(this.serviceData);
        dest.writeTypedList(this.contentProviderData);
        dest.writeTypedList(this.broadcastReceiverData);
        dest.writeParcelable(this.permissionData, flags);
    }

    protected AppDetailData(Parcel in) {
        this.generalData = in.readParcelable(GeneralData.class.getClassLoader());
        this.certificateData = in.readParcelable(CertificateData.class.getClassLoader());
        this.activityData = in.createTypedArrayList(ActivityData.CREATOR);
        this.serviceData = in.createTypedArrayList(ServiceData.CREATOR);
        this.contentProviderData = in.createTypedArrayList(ContentProviderData.CREATOR);
        this.broadcastReceiverData = in.createTypedArrayList(BroadcastReceiverData.CREATOR);
        this.permissionData = in.readParcelable(PermissionData.class.getClassLoader());
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
