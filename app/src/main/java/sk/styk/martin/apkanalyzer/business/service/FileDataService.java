package sk.styk.martin.apkanalyzer.business.service;

import android.content.pm.PackageInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import sk.styk.martin.apkanalyzer.model.detail.FileData;
import sk.styk.martin.apkanalyzer.model.detail.FileEntry;

/**
 * Created by Martin Styk on 30.06.2017.
 */
public class FileDataService {

    @NonNull
    public FileData get(@NonNull PackageInfo packageInfo) {

        FileData fileData = new FileData();

        Manifest mf = openManifest(packageInfo.applicationInfo.sourceDir);

        if (mf == null) {
            return fileData;
        }

        List<FileEntry> drawables = new ArrayList<>();
        List<FileEntry> layouts = new ArrayList<>();
        List<FileEntry> menus = new ArrayList<>();
        List<FileEntry> others = new ArrayList<>();

        Map<String, Attributes> map = mf.getEntries();

        for (Map.Entry<String, Attributes> entry : map.entrySet()) {
            String fileName = entry.getKey();
            String hash = (entry.getValue() == null) ? null : entry.getValue().getValue("SHA1-Digest");
            FileEntry fileEntry = new FileEntry(fileName, hash);

            if (fileName.startsWith("res/drawable")) {
                drawables.add(fileEntry);

            } else if (fileName.startsWith("res/layout")) {
                layouts.add(fileEntry);

            } else if (fileName.startsWith("res/menu")) {
                menus.add(fileEntry);

            } else if (fileName.equals("classes.dex")) {
                fileData.setDexHash(hash);

            } else if (fileName.equals("resources.arsc")) {
                fileData.setArscHash(hash);

            } else if (fileName.equals("AndroidManifest.xml")) {
                fileData.setManifestHash(hash);

            } else {
                others.add(fileEntry);
            }
        }

        fileData.setDrawableHashes(drawables);
        fileData.setLayoutHashes(layouts);
        fileData.setMenuHashes(menus);
        fileData.setOtherHashes(others);

        return fileData;
    }

    public int getNumberOfFiles(@NonNull PackageInfo packageInfo) {
        Manifest mf = openManifest(packageInfo.applicationInfo.sourceDir);

        return mf == null ? 0 : mf.getEntries().size();
    }

    /**
     * Gets all entries from application manifest file
     *
     * @param source APK package location
     * @return all entries on manifest file or null, manifest is not found
     */
    @Nullable
    private Manifest openManifest(String source) {
        Manifest mf = null;
        try {
            JarFile jar = new JarFile(source);
            mf = jar.getManifest();
        } catch (IOException e) {
            Log.e(FileDataService.class.getSimpleName(), "Unable to find manifest", e);
        }
        return mf;
    }
}
