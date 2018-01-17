package sk.styk.martin.apkanalyzer.model.detail;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Martin Styk
 * @version 22.06.2017.
 */
public class ContentProviderData implements Parcelable {

    private String name;

    //he name provider is published under content://
    private String authority;

    //Optional permission required for read-only access this content provider
    private String readPermission;

    //Optional permission required for read/write access this content provider
    private String writePermission;

    //May be called by another activity
    private boolean exported;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getReadPermission() {
        return readPermission;
    }

    public void setReadPermission(String readPermission) {
        this.readPermission = readPermission;
    }

    public String getWritePermission() {
        return writePermission;
    }

    public void setWritePermission(String writePermission) {
        this.writePermission = writePermission;
    }

    public boolean isExported() {
        return exported;
    }

    public void setExported(boolean exported) {
        this.exported = exported;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContentProviderData that = (ContentProviderData) o;

        if (exported != that.exported) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (authority != null ? !authority.equals(that.authority) : that.authority != null)
            return false;
        if (readPermission != null ? !readPermission.equals(that.readPermission) : that.readPermission != null)
            return false;
        return writePermission != null ? writePermission.equals(that.writePermission) : that.writePermission == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (authority != null ? authority.hashCode() : 0);
        result = 31 * result + (readPermission != null ? readPermission.hashCode() : 0);
        result = 31 * result + (writePermission != null ? writePermission.hashCode() : 0);
        result = 31 * result + (exported ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ContentProviderData{" +
                "name='" + name + '\'' +
                ", authority='" + authority + '\'' +
                ", readPermission='" + readPermission + '\'' +
                ", writePermission='" + writePermission + '\'' +
                ", exported=" + exported +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.authority);
        dest.writeString(this.readPermission);
        dest.writeString(this.writePermission);
        dest.writeByte(this.exported ? (byte) 1 : (byte) 0);
    }

    public ContentProviderData() {
    }

    protected ContentProviderData(Parcel in) {
        this.name = in.readString();
        this.authority = in.readString();
        this.readPermission = in.readString();
        this.writePermission = in.readString();
        this.exported = in.readByte() != 0;
    }

    public static final Creator<ContentProviderData> CREATOR = new Creator<ContentProviderData>() {
        @Override
        public ContentProviderData createFromParcel(Parcel source) {
            return new ContentProviderData(source);
        }

        @Override
        public ContentProviderData[] newArray(int size) {
            return new ContentProviderData[size];
        }
    };
}

