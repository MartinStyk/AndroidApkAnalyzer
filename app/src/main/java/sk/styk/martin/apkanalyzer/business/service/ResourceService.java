package sk.styk.martin.apkanalyzer.business.service;

import android.support.annotation.NonNull;

import java.util.HashSet;
import java.util.Set;

import sk.styk.martin.apkanalyzer.model.detail.FileData;
import sk.styk.martin.apkanalyzer.model.detail.FileEntry;
import sk.styk.martin.apkanalyzer.model.detail.ResourceData;

/**
 * Created by Martin Styk on 22.06.2017.
 */

public class ResourceService {

    public ResourceData get(@NonNull FileData fileData) {
        ResourceData data = new ResourceData();

        int numJpg = 0;
        int numGif = 0;
        int numPng = 0;
        int numXml = 0;
        int numNinePatchPng = 0;

        int ldpi = 0;
        int mdpi = 0;
        int hdpi = 0;
        int xhdpi = 0;
        int xxhdpi = 0;
        int xxxhdpi = 0;
        int nodpi = 0;
        int tvdpi = 0;

        Set<String> drawables = new HashSet<>();
        Set<String> layouts = new HashSet<>();

        for (FileEntry file : fileData.getDrawableHashes()) {
            String name = file.getPath();

            // eliminate duplicities
            drawables.add(file.getFileName());

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
        }

        // just adding it to set to eliminate duplicities
        for (FileEntry file : fileData.getLayoutHashes()) {
            layouts.add(file.getFileName());
        }


        data.setDrawables(fileData.getDrawableHashes().size());
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
        data.setLayouts(fileData.getLayoutHashes().size());
        data.setDifferentDrawables(drawables.size());
        data.setDifferentLayouts(layouts.size());

        return data;
    }
}
