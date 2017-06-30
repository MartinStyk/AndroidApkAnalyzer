package sk.styk.martin.apkanalyzer.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Martin Styk on 22.06.2017.
 */
public class ServiceData implements Parcelable {

    private String name;

    //Optional name of a permission required to be able to access this Service
    private String permission;

    //May be called by another activity
    private boolean exported;

    //The service will automatically be stopped by the system if the user removes a task that is rooted in one of the application's activities
    private boolean stopWithTask;

    //Single instance of the service will run for all users on the device
    private boolean singleUser;

    //The service will run in its own isolated process
    private boolean isolatedProcess;

    //The service can be bound and run in the calling application's package, rather than the package in which it is declared
    private boolean externalService;

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

    public boolean isStopWithTask() {
        return stopWithTask;
    }

    public void setStopWithTask(boolean stopWithTask) {
        this.stopWithTask = stopWithTask;
    }

    public boolean isSingleUser() {
        return singleUser;
    }

    public void setSingleUser(boolean singleUser) {
        this.singleUser = singleUser;
    }

    public boolean isolatedProcess() {
        return isolatedProcess;
    }

    public void setIsolatedProcess(boolean isolatedProcess) {
        this.isolatedProcess = isolatedProcess;
    }

    public boolean isExternalService() {
        return externalService;
    }

    public void setExternalService(boolean externalService) {
        this.externalService = externalService;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServiceData that = (ServiceData) o;

        if (exported != that.exported) return false;
        if (stopWithTask != that.stopWithTask) return false;
        if (singleUser != that.singleUser) return false;
        if (isolatedProcess != that.isolatedProcess) return false;
        if (externalService != that.externalService) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return permission != null ? permission.equals(that.permission) : that.permission == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (permission != null ? permission.hashCode() : 0);
        result = 31 * result + (exported ? 1 : 0);
        result = 31 * result + (stopWithTask ? 1 : 0);
        result = 31 * result + (singleUser ? 1 : 0);
        result = 31 * result + (isolatedProcess ? 1 : 0);
        result = 31 * result + (externalService ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ServiceData{" +
                "name='" + name + '\'' +
                ", permission='" + permission + '\'' +
                ", exported=" + exported +
                ", stopWithTask=" + stopWithTask +
                ", singleUser=" + singleUser +
                ", isolatedProcess=" + isolatedProcess +
                ", externalService=" + externalService +
                '}';
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
        dest.writeByte(this.stopWithTask ? (byte) 1 : (byte) 0);
        dest.writeByte(this.singleUser ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isolatedProcess ? (byte) 1 : (byte) 0);
        dest.writeByte(this.externalService ? (byte) 1 : (byte) 0);
    }

    public ServiceData() {
    }

    protected ServiceData(Parcel in) {
        this.name = in.readString();
        this.permission = in.readString();
        this.exported = in.readByte() != 0;
        this.stopWithTask = in.readByte() != 0;
        this.singleUser = in.readByte() != 0;
        this.isolatedProcess = in.readByte() != 0;
        this.externalService = in.readByte() != 0;
    }

    public static final Creator<ServiceData> CREATOR = new Creator<ServiceData>() {
        @Override
        public ServiceData createFromParcel(Parcel source) {
            return new ServiceData(source);
        }

        @Override
        public ServiceData[] newArray(int size) {
            return new ServiceData[size];
        }
    };
}

