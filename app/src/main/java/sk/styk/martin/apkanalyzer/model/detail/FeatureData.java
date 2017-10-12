package sk.styk.martin.apkanalyzer.model.detail;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Martin Styk on 12.10.2017.
 */

public class FeatureData implements Parcelable {
    public static final Creator<FeatureData> CREATOR = new Creator<FeatureData>() {
        @Override
        public FeatureData createFromParcel(Parcel source) {
            return new FeatureData(source);
        }

        @Override
        public FeatureData[] newArray(int size) {
            return new FeatureData[size];
        }
    };
    private String name;
    private boolean required;

    public FeatureData() {
    }

    protected FeatureData(Parcel in) {
        this.name = in.readString();
        this.required = in.readByte() != 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeatureData that = (FeatureData) o;

        if (required != that.required) return false;
        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public String toString() {
        return "FeatureData{" +
                "name='" + name + '\'' +
                ", required=" + required +
                '}';
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (required ? 1 : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeByte(this.required ? (byte) 1 : (byte) 0);
    }
}
