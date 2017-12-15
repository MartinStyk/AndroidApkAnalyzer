package sk.styk.martin.apkanalyzer.business.service;

import android.content.pm.PackageInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

        Manifest mf = openManifest(packageInfo);

        if (mf == null) {
            return fileData;
        }

        List<FileEntry> drawables = new ArrayList<>();
        List<FileEntry> layouts = new ArrayList<>();
        List<FileEntry> menus = new ArrayList<>();
        List<FileEntry> others = new ArrayList<>();

        int numberPngs = 0, numberXmls = 0, numberPngsWithDifferentName = 0, numberXmlsWithDifferentName = 0;
        Set<String> pngsSet = new HashSet<>();
        Set<String> xmlsSet = new HashSet<>();

        Map<String, Attributes> map = mf.getEntries();

        for (Map.Entry<String, Attributes> entry : map.entrySet()) {
            String fileName = entry.getKey();
            String hash = extractHash(entry);
            FileEntry fileEntry = new FileEntry(fileName, hash);

            // sort into categories according to location
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

            // sort into categories according to file type
            if (fileName.endsWith(".png")) {
                numberPngs++;
                pngsSet.add(fileEntry.getFileName());
            } else if (fileName.endsWith(".xml")) {
                numberXmls++;
                xmlsSet.add(fileEntry.getFileName());
            }

        }

        fileData.setDrawableHashes(drawables);
        fileData.setLayoutHashes(layouts);
        fileData.setMenuHashes(menus);
        fileData.setOtherHashes(others);

        fileData.setNumberPngs(numberPngs);
        fileData.setNumberPngsWithDifferentName(pngsSet.size());
        fileData.setNumberXmls(numberXmls);
        fileData.setNumberXmlsWithDifferentName(xmlsSet.size());

        return fileData;
    }

    /**
     * Gets all entries from application manifest file
     *
     * @param packageInfo APK packageInfo
     * @return all entries on manifest file or null, manifest is not found
     */
    @Nullable
    private Manifest openManifest(PackageInfo packageInfo) {
        if(packageInfo.applicationInfo == null || packageInfo.applicationInfo.sourceDir == null)
            return null;

        Manifest mf = null;
        try {
            JarFile jar = new JarFile(packageInfo.applicationInfo.sourceDir);
            mf = jar.getManifest();
        } catch (IOException e) {
            Log.e(FileDataService.class.getSimpleName(), "Unable to find manifest", e);
        }
        return mf;
    }

    private String extractHash(Map.Entry<String, Attributes> entry) {
        String sha1 = (entry.getValue() == null) ? null : entry.getValue().getValue("SHA1-Digest");
        if (sha1 != null)
            return sha1;

        String sha256 = (entry.getValue() == null) ? null : entry.getValue().getValue("SHA-256-Digest");
        return sha256;
    }
}
