package sk.styk.martin.apkanalyzer.model.detail;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin Styk on 02.07.2017.
 */
public class FileData implements Parcelable {

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
    /**
     * hash of classes.dex file
     */
    private String dexHash;
    /**
     * hash of resources.arsc file
     */
    private String arscHash;
    /**
     * hash of AndroidManifest.xml file
     */
    private String manifestHash;
    /**
     * Files in res/drawable* directories
     */
    private List<FileEntry> drawableHashes = new ArrayList<>(0);
    /**
     * Files in res/layout* directories
     */
    private List<FileEntry> layoutHashes = new ArrayList<>(0);
    /**
     * Files in assets/* directories
     */
    private List<FileEntry> menuHashes = new ArrayList<>(0);
    /**
     * All remaining files
     */
    private List<FileEntry> otherHashes = new ArrayList<>(0);

    public FileData() {
    }

    protected FileData(Parcel in) {
        this.dexHash = in.readString();
        this.arscHash = in.readString();
        this.manifestHash = in.readString();
        this.drawableHashes = in.createTypedArrayList(FileEntry.CREATOR);
        this.layoutHashes = in.createTypedArrayList(FileEntry.CREATOR);
        this.menuHashes = in.createTypedArrayList(FileEntry.CREATOR);
        this.otherHashes = in.createTypedArrayList(FileEntry.CREATOR);
    }

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

    public List<FileEntry> getDrawableHashes() {
        return drawableHashes;
    }

    public void setDrawableHashes(List<FileEntry> drawableHashes) {
        this.drawableHashes = drawableHashes;
    }

    public List<FileEntry> getPngHashes() {
        List<FileEntry> pngs = new ArrayList<>(drawableHashes.size());
        for (FileEntry entry : drawableHashes) {
            if (entry.getPath().endsWith(".png"))
                pngs.add(entry);
        }
        for (FileEntry entry : otherHashes) {
            if (entry.getPath().endsWith(".png"))
                pngs.add(entry);
        }
        return pngs;
    }

    public List<FileEntry> getLayoutHashes() {
        return layoutHashes;
    }

    public void setLayoutHashes(List<FileEntry> layoutHashes) {
        this.layoutHashes = layoutHashes;
    }

    public List<FileEntry> getMenuHashes() {
        return menuHashes;
    }

    public void setMenuHashes(List<FileEntry> menuHashes) {
        this.menuHashes = menuHashes;
    }

    public List<FileEntry> getOtherHashes() {
        return otherHashes;
    }

    public void setOtherHashes(List<FileEntry> otherHashes) {
        this.otherHashes = otherHashes;
    }

    public String getManifestHash() {
        return manifestHash;
    }

    public void setManifestHash(String manifestHash) {
        this.manifestHash = manifestHash;
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
        if (manifestHash != null ? !manifestHash.equals(fileData.manifestHash) : fileData.manifestHash != null)
            return false;
        if (drawableHashes != null ? !drawableHashes.equals(fileData.drawableHashes) : fileData.drawableHashes != null)
            return false;
        if (layoutHashes != null ? !layoutHashes.equals(fileData.layoutHashes) : fileData.layoutHashes != null)
            return false;
        if (menuHashes != null ? !menuHashes.equals(fileData.menuHashes) : fileData.menuHashes != null)
            return false;
        return otherHashes != null ? otherHashes.equals(fileData.otherHashes) : fileData.otherHashes == null;
    }

    @Override
    public int hashCode() {
        int result = dexHash != null ? dexHash.hashCode() : 0;
        result = 31 * result + (arscHash != null ? arscHash.hashCode() : 0);
        result = 31 * result + (manifestHash != null ? manifestHash.hashCode() : 0);
        result = 31 * result + (drawableHashes != null ? drawableHashes.hashCode() : 0);
        result = 31 * result + (layoutHashes != null ? layoutHashes.hashCode() : 0);
        result = 31 * result + (menuHashes != null ? menuHashes.hashCode() : 0);
        result = 31 * result + (otherHashes != null ? otherHashes.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FileData{" +
                "dexHash='" + dexHash + '\'' +
                ", arscHash='" + arscHash + '\'' +
                ", manifestHash='" + manifestHash + '\'' +
                ", drawableHashes=" + drawableHashes +
                ", layoutHashes=" + layoutHashes +
                ", menuHashes=" + menuHashes +
                ", otherHashes=" + otherHashes +
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
        dest.writeString(this.manifestHash);
        dest.writeTypedList(this.drawableHashes);
        dest.writeTypedList(this.layoutHashes);
        dest.writeTypedList(this.menuHashes);
        dest.writeTypedList(this.otherHashes);
    }

    @NonNull
    public List<String> getOnlyHash(@NonNull List<FileEntry> fileEntries) {
        List<String> result = new ArrayList<>(fileEntries.size());
        for (FileEntry fileEntry : fileEntries) {
            result.add(fileEntry.getHash());
        }
        return result;
    }

    public int getTotalFiles() {
        return 3 + otherHashes.size() + drawableHashes.size() + layoutHashes.size() + menuHashes.size();
    }
}
