package sk.styk.martin.apkanalyzer.model.detail;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Martin Styk on 06.11.2017.
 */
public class FileEntry implements Parcelable {

    private String path;
    private String hash;

    public FileEntry(String path, String hash){
        this.path = path;
        this.hash = hash;
    }

    public String getPath() {
        return path;
    }

    public String getHash() {
        return hash;
    }

    @Override
    public String toString() {
        return "FileEntry{" +
                "path='" + path + '\'' +
                ", hash='" + hash + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileEntry fileEntry = (FileEntry) o;

        if (path != null ? !path.equals(fileEntry.path) : fileEntry.path != null) return false;
        return hash != null ? hash.equals(fileEntry.hash) : fileEntry.hash == null;
    }

    @Override
    public int hashCode() {
        int result = path != null ? path.hashCode() : 0;
        result = 31 * result + (hash != null ? hash.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeString(this.hash);
    }

    protected FileEntry(Parcel in) {
        this.path = in.readString();
        this.hash = in.readString();
    }

    public static final Parcelable.Creator<FileEntry> CREATOR = new Parcelable.Creator<FileEntry>() {
        @Override
        public FileEntry createFromParcel(Parcel source) {
            return new FileEntry(source);
        }

        @Override
        public FileEntry[] newArray(int size) {
            return new FileEntry[size];
        }
    };
}