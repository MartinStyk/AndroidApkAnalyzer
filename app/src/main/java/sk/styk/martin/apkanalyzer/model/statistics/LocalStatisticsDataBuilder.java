package sk.styk.martin.apkanalyzer.model.statistics;

import java.util.HashMap;
import java.util.Map;

import sk.styk.martin.apkanalyzer.util.MathStatistics;
import sk.styk.martin.apkanalyzer.util.PercentagePair;

/**
 * Created by Martin Styk on 28.07.2017.
 */
public class LocalStatisticsDataBuilder {

    private int totalApplications = 0;

    private int systemApps;
    private Map<Integer, Integer> installLocation = new HashMap<>(3);
    private Map<Integer, Integer> targetSdk = new HashMap<>(26);
    private Map<Integer, Integer> minSdk = new HashMap<>(26);
    private float[] apkSize;

    private Map<String, Integer> signAlgorithm = new HashMap<>(5);

    private float[] activities;
    private float[] services;
    private float[] providers;
    private float[] receivers;

    private float[] usedPermissions;
    private float[] definedPermissions;

    private float[] files;

    private float[] drawables;
    private float[] differentDrawables;

    private float[] layouts;
    private float[] differentLayouts;

    public LocalStatisticsDataBuilder(int datasetSize) {
        apkSize = new float[datasetSize];
        activities = new float[datasetSize];
        services = new float[datasetSize];
        providers = new float[datasetSize];
        receivers = new float[datasetSize];
        usedPermissions = new float[datasetSize];
        definedPermissions = new float[datasetSize];
        files = new float[datasetSize];
        drawables = new float[datasetSize];
        differentDrawables = new float[datasetSize];
        layouts = new float[datasetSize];
        differentLayouts = new float[datasetSize];
    }

    public LocalStatisticsData build() {
        LocalStatisticsData data = new LocalStatisticsData();
        data.setTotalApplications(totalApplications);
        data.setSystemApps(new PercentagePair(totalApplications, systemApps));
        data.setInstallLocation(getPercentagePairMap(installLocation));
        data.setTargetSdk(getPercentagePairMap(targetSdk));
        data.setMinSdk(getPercentagePairMap(minSdk));

        data.setApkSize(new MathStatistics(apkSize));

        data.setSignAlgorithm(getPercentagePairMap(signAlgorithm));

        data.setActivites(new MathStatistics(activities));
        data.setServices(new MathStatistics(services));
        data.setReceivers(new MathStatistics(receivers));
        data.setProviders(new MathStatistics(providers));

        data.setUsedPermissions(new MathStatistics(usedPermissions));
        data.setDefinedPermissions(new MathStatistics(definedPermissions));
        data.setFiles(new MathStatistics(files));

        data.setDrawables(new MathStatistics(drawables));
        data.setDifferentDrawables(new MathStatistics(differentDrawables));

        data.setLayouts(new MathStatistics(layouts));
        data.setDifferentLayouts(new MathStatistics(differentLayouts));

        return data;
    }

    public void add(LocalStatisticsAppData appData) {
        totalApplications++;
        if (appData.isSystemApp()) systemApps++;
        addToMap(installLocation, Integer.valueOf(appData.getInstallLocation()));
        addToMap(targetSdk, appData.getTargetSdk());
        addToMap(minSdk, appData.getMinSdk());
        apkSize[totalApplications] = appData.getApkSize();
        addToMap(signAlgorithm, appData.getSignAlgorithm());

        activities[totalApplications] = appData.getActivities();
        services[totalApplications] = appData.getServices();
        providers[totalApplications] = appData.getProviders();
        receivers[totalApplications] = appData.getReceivers();

        usedPermissions[totalApplications] = appData.getUsedPermissions();
        definedPermissions[totalApplications] = appData.getDefinedPermissions();

        files[totalApplications] = appData.getFiles();

        drawables[totalApplications] = appData.getDrawables();
        differentDrawables[totalApplications] = appData.getDifferentDrawables();

        layouts[totalApplications] = appData.getLayouts();
        differentLayouts[totalApplications] = appData.getDifferentLayouts();
    }

    private <T> void addToMap(Map<T, Integer> map, T key) {
        Integer value;
        if ((value = map.get(key)) != null) {
            value++;
            map.put(key, value);
        } else {
            map.put(key, 0);
        }
    }

    private <T> Map<T, PercentagePair> getPercentagePairMap(Map<T, Integer> map) {
        Map<T, PercentagePair> finalMap = new HashMap<>(map.size());
        for (Map.Entry<T, Integer> entry : map.entrySet()) {
            finalMap.put(entry.getKey(), new PercentagePair(entry.getValue(), totalApplications));
        }
        return finalMap;
    }

}
