package sk.styk.martin.apkanalyzer.model.detail;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Single used permission entry
 * <p>
 * @author Martin Styk
 * @version 22.06.2017.
 */
public class PermissionData implements Parcelable {

    public static final Creator<PermissionData> CREATOR = new Creator<PermissionData>() {
        @Override
        public PermissionData createFromParcel(Parcel source) {
            return new PermissionData(source);
        }

        @Override
        public PermissionData[] newArray(int size) {
            return new PermissionData[size];
        }
    };
    private String name;
    private String simpleName;
    private String groupName;
    private int protectionLevel;

    public PermissionData(String name, String groupName, int protectionLevel) {
        this.name = name;
        this.groupName = groupName;
        this.protectionLevel = protectionLevel;
        this.simpleName = createSimpleName(name);
    }

    protected PermissionData(Parcel in) {
        this.name = in.readString();
        this.simpleName = in.readString();
        this.groupName = in.readString();
        this.protectionLevel = in.readInt();
    }

    private String createSimpleName(String name) {
        StringBuilder simpleNameBuilder = new StringBuilder(name);

        int lastDot = name.lastIndexOf(".");
        if (lastDot > 0 && lastDot < name.length())
            simpleNameBuilder = new StringBuilder(name.substring(lastDot + 1));

        int i = 0;
        boolean previousSpace = false;
        while (++i < simpleNameBuilder.length()) {
            if (simpleNameBuilder.charAt(i) == '_') {
                simpleNameBuilder.replace(i, i + 1, " ");
                previousSpace = true;
            } else {
                if (!previousSpace) {
                    char lowercase = Character.toLowerCase(simpleNameBuilder.charAt(i));
                    simpleNameBuilder.replace(i, i + 1, String.valueOf(lowercase));
                }
                previousSpace = false;
            }
        }
        return simpleNameBuilder.toString();
    }

    public String getName() {
        return name;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public String getGroupName() {
        return groupName;
    }

    public int getProtectionLevel() {
        return protectionLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PermissionData that = (PermissionData) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.simpleName);
        dest.writeString(this.groupName);
        dest.writeInt(this.protectionLevel);
    }
}