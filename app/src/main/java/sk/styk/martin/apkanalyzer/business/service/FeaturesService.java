package sk.styk.martin.apkanalyzer.business.service;

import android.content.pm.FeatureInfo;
import android.content.pm.PackageInfo;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import sk.styk.martin.apkanalyzer.model.detail.FeatureData;

/**
 * @author Martin Styk
 * @version 12.10.2017.
 */
public class FeaturesService {

    public List<FeatureData> get(@NonNull PackageInfo packageInfo) {

        FeatureInfo[] featureInfos = packageInfo.reqFeatures;
        if (featureInfos == null) {
            return new ArrayList<>(0);
        }

        List<FeatureData> requestedFeatures = new ArrayList<>(featureInfos.length);

        for (FeatureInfo featureInfo : featureInfos) {
            FeatureData data = new FeatureData();
            String name = featureInfo.name == null ? featureInfo.getGlEsVersion() : featureInfo.name;
            data.setName(name);
            data.setRequired(featureInfo.flags == FeatureInfo.FLAG_REQUIRED);

            requestedFeatures.add(data);
        }

        return requestedFeatures;
    }
}
