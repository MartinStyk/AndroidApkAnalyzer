package sk.styk.martin.apkanalyzer.model.detail;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Martin Styk on 02.07.2017.
 */
public class FileData implements Parcelable {

    /**
     * hash of classes.dex file
     */
    private String dexHash;

    /**
     * hash of resources.arsc file
     */
    private String arscHash;


    private Map<String, String> allHashes;

    public String getDexHash() {
        return dexHash;
    }

    public void setDexHash(String dexHash) {
        this.dexHash = dexHash;
    }

    public String getArscHash() {
        return arscHash;
    }

    public void setArscHash(String arscHash) {
        this.arscHash = arscHash;
    }

    public Map<String, String> getAllHashes() {
        return allHashes;
    }

    public void setAllHashes(Map<String, String> allHashes) {
        this.allHashes = allHashes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileData fileData = (FileData) o;

        if (dexHash != null ? !dexHash.equals(fileData.dexHash) : fileData.dexHash != null)
            return false;
        if (arscHash != null ? !arscHash.equals(fileData.arscHash) : fileData.arscHash != null)
            return false;
        return allHashes != null ? allHashes.equals(fileData.allHashes) : fileData.allHashes == null;

    }

    @Override
    public int hashCode() {
        int result = dexHash != null ? dexHash.hashCode() : 0;
        result = 31 * result + (arscHash != null ? arscHash.hashCode() : 0);
        result = 31 * result + (allHashes != null ? allHashes.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FileData{" +
                "dexHash='" + dexHash + '\'' +
                ", arscHash='" + arscHash + '\'' +
                ", allHashes=" + allHashes +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.dexHash);
        dest.writeString(this.arscHash);
        dest.writeInt(this.allHashes.size());
        for (Map.Entry<String, String> entry : this.allHashes.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
    }

    public FileData() {
    }

    protected FileData(Parcel in) {
        this.dexHash = in.readString();
        this.arscHash = in.readString();
        int allHashesSize = in.readInt();
        this.allHashes = new HashMap<String, String>(allHashesSize);
        for (int i = 0; i < allHashesSize; i++) {
            String key = in.readString();
            String value = in.readString();
            this.allHashes.put(key, value);
        }
    }

    public static final Creator<FileData> CREATOR = new Creator<FileData>() {
        @Override
        public FileData createFromParcel(Parcel source) {
            return new FileData(source);
        }

        @Override
        public FileData[] newArray(int size) {
            return new FileData[size];
        }
    };
}
