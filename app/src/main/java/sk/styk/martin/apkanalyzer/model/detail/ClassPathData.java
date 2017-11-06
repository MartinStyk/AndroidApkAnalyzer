package sk.styk.martin.apkanalyzer.model.detail;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin Styk on 06.11.2017.
 */

public class ClassPathData implements Parcelable {
    private List<String> packageClasses;
    private List<String> otherClasses;

    public List<String> getPackageClasses() {
        return packageClasses;
    }

    public void setPackageClasses(List<String> packageClasses) {
        this.packageClasses = packageClasses;
    }

    public List<String> getOtherClasses() {
        return otherClasses;
    }

    public void setOtherClasses(List<String> otherClasses) {
        this.otherClasses = otherClasses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassPathData that = (ClassPathData) o;

        if (packageClasses != null ? !packageClasses.equals(that.packageClasses) : that.packageClasses != null)
            return false;
        return otherClasses != null ? otherClasses.equals(that.otherClasses) : that.otherClasses == null;
    }

    @Override
    public int hashCode() {
        int result = packageClasses != null ? packageClasses.hashCode() : 0;
        result = 31 * result + (otherClasses != null ? otherClasses.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.packageClasses);
        dest.writeStringList(this.otherClasses);
    }

    public ClassPathData() {
    }

    protected ClassPathData(Parcel in) {
        this.packageClasses = in.createStringArrayList();
        this.otherClasses = in.createStringArrayList();
    }

    public static final Parcelable.Creator<ClassPathData> CREATOR = new Parcelable.Creator<ClassPathData>() {
        @Override
        public ClassPathData createFromParcel(Parcel source) {
            return new ClassPathData(source);
        }

        @Override
        public ClassPathData[] newArray(int size) {
            return new ClassPathData[size];
        }
    };
}
