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

    private AppBasicData appBasicData;

    private CertificateData certificateData;

    public AppDetailData(){
    }

    public AppBasicData getAppBasicData() {
        return appBasicData;
    }

    public void setAppBasicData(AppBasicData appBasicData) {
        this.appBasicData = appBasicData;
    }

    public CertificateData getCertificateData() {
        return certificateData;
    }

    public void setCertificateData(CertificateData certificateData) {
        this.certificateData = certificateData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.appBasicData, flags);
    }

    protected AppDetailData(Parcel in) {
        this.appBasicData = in.readParcelable(AppBasicData.class.getClassLoader());
    }

    public static final Parcelable.Creator<AppDetailData> CREATOR = new Parcelable.Creator<AppDetailData>() {
        @Override
        public AppDetailData createFromParcel(Parcel source) {
            return new AppDetailData(source);
        }

        @Override
        public AppDetailData[] newArray(int size) {
            return new AppDetailData[size];
        }
    };

    @Override
    public String toString() {
        return "AppDetailData{" +
                "appBasicData=" + appBasicData +
                ", certificateData=" + certificateData +
                '}';
    }
}
