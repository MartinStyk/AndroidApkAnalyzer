package sk.styk.martin.apkanalyzer.business.service;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import sk.styk.martin.apkanalyzer.model.detail.FileData;
import sk.styk.martin.apkanalyzer.model.detail.ResourceData;

/**
 * Created by Martin Styk on 22.06.2017.
 */

public class ResourceService {

    private PackageManager packageManager;

    public ResourceService(@NonNull PackageManager packageManager) {
        this.packageManager = packageManager;
    }

    public ResourceData get(@NonNull FileData fileData) {
        ResourceData data = new ResourceData();
        Map<String, String> allFiles = fileData.getAllHashes();

        int numJpg = 0;
        int numGif = 0;
        int numPng = 0;
        int numXml = 0;
        int numNinePatchPng = 0;

        int numDrawables = 0;

        int ldpi = 0;
        int mdpi = 0;
        int hdpi = 0;
        int xhdpi = 0;
        int xxhdpi = 0;
        int xxxhdpi = 0;
        int nodpi = 0;
        int tvdpi = 0;

        int numLayouts = 0;

        Set<String> drawables = new HashSet<>();
        Set<String> layouts = new HashSet<>();

        for (Map.Entry<String, String> file : allFiles.entrySet()) {
            String name = file.getKey();

            if (name.startsWith("res/drawable")) {
                int startIndexName = name.lastIndexOf("/");
                String fileName = name.substring(startIndexName, name.length());
                drawables.add(fileName);
                numDrawables ++;

                if (name.endsWith(".jpg")) numJpg++;
                else if (name.endsWith(".gif")) numGif++;
                else if (name.endsWith(".9.png")) numNinePatchPng++;
                else if (name.endsWith(".png")) numPng++;
                else if (name.endsWith(".xml")) numXml++;

                if (name.contains("ldpi")) ldpi++;
                else if (name.contains("mdpi")) mdpi++;
                else if (name.contains("xxxhdpi")) xxxhdpi++;
                else if (name.contains("xxhdpi")) xxhdpi++;
                else if (name.contains("xhdpi")) xhdpi++;
                else if (name.contains("hdpi")) hdpi++;
                else if (name.contains("nodpi")) nodpi++;
                else if (name.contains("tvdpi")) tvdpi++;
                else mdpi++;

            } else if (name.startsWith("res/layout")) {
                numLayouts++;
                int startIndexName = name.lastIndexOf("/");
                String fileName = name.substring(startIndexName, name.length());
                layouts.add(fileName);
            }
        }

        data.setDrawables(numDrawables);
        data.setPngDrawables(numPng);
        data.setNinePatchDrawables(numNinePatchPng);
        data.setJpgDrawables(numJpg);
        data.setGifDrawables(numGif);
        data.setXmlDrawables(numXml);
        data.setLdpiDrawables(ldpi);
        data.setMdpiDrawables(mdpi);
        data.setHdpiDrawables(hdpi);
        data.setXhdpiDrawables(xhdpi);
        data.setXxhdpiDrawables(xxhdpi);
        data.setXxxhdpiDrawables(xxxhdpi);
        data.setNodpiDrawables(nodpi);
        data.setTvdpiDrawables(tvdpi);
        data.setLayouts(numLayouts);
        data.setDifferentDrawables(drawables.size());
        data.setDifferentLayouts(layouts.size());

        return data;
    }

}
