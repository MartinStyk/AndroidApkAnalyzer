package sk.styk.martin.apkanalyzer.model.detail;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Martin Styk
 * @version 22.06.2017.
 */
public class BroadcastReceiverData implements Parcelable {

    private String name;

    //Name of permission required to access this receiver
    private String permission;

    //May be called by another activity
    private boolean exported;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public boolean isExported() {
        return exported;
    }

    public void setExported(boolean exported) {
        this.exported = exported;
    }

    @Override
    public String toString() {
        return "BroadcastReceiverData{" +
                "name='" + name + '\'' +
                ", permission='" + permission + '\'' +
                ", exported=" + exported +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BroadcastReceiverData that = (BroadcastReceiverData) o;

        if (exported != that.exported) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return permission != null ? permission.equals(that.permission) : that.permission == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (permission != null ? permission.hashCode() : 0);
        result = 31 * result + (exported ? 1 : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.permission);
        dest.writeByte(this.exported ? (byte) 1 : (byte) 0);
    }

    public BroadcastReceiverData() {
    }

    protected BroadcastReceiverData(Parcel in) {
        this.name = in.readString();
        this.permission = in.readString();
        this.exported = in.readByte() != 0;
    }

    public static final Creator<BroadcastReceiverData> CREATOR = new Creator<BroadcastReceiverData>() {
        @Override
        public BroadcastReceiverData createFromParcel(Parcel source) {
            return new BroadcastReceiverData(source);
        }

        @Override
        public BroadcastReceiverData[] newArray(int size) {
            return new BroadcastReceiverData[size];
        }
    };
}

