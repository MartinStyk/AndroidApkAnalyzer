package sk.styk.martin.apkanalyzer.model.statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sk.styk.martin.apkanalyzer.model.detail.AppSource;
import sk.styk.martin.apkanalyzer.util.AndroidVersionHelper;
import sk.styk.martin.apkanalyzer.util.InstallLocationHelper;
import sk.styk.martin.apkanalyzer.util.MathStatistics;
import sk.styk.martin.apkanalyzer.util.PercentagePair;

/**
 * Created by Martin Styk on 28.07.2017.
 */
public class LocalStatisticsDataBuilder {

    private int analyzeSuccess = 0;
    private int analyzeFailed = 0;

    private int systemApps;
    private Map<String, List<String>> installLocation = new HashMap<>(3);
    private Map<Integer, List<String>> targetSdk = new HashMap<>(AndroidVersionHelper.MAX_SDK_VERSION);
    private Map<Integer, List<String>> minSdk = new HashMap<>(AndroidVersionHelper.MAX_SDK_VERSION);
    private Map<AppSource, List<String>> appSource = new HashMap<>(AppSource.values().length);

    private float[] apkSize;

    private Map<String, List<String>> signAlgorithm = new HashMap<>(5);

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
        int arraySize = datasetSize + 1;
        apkSize = new float[arraySize];
        activities = new float[arraySize];
        services = new float[arraySize];
        providers = new float[arraySize];
        receivers = new float[arraySize];
        usedPermissions = new float[arraySize];
        definedPermissions = new float[arraySize];
        files = new float[arraySize];
        drawables = new float[arraySize];
        differentDrawables = new float[arraySize];
        layouts = new float[arraySize];
        differentLayouts = new float[arraySize];
    }

    public LocalStatisticsData build() {
        LocalStatisticsData data = new LocalStatisticsData();
        data.setAnalyzeSuccess(new PercentagePair(analyzeSuccess, analyzeSuccess + analyzeFailed));
        data.setAnalyzeFailed(new PercentagePair(analyzeFailed, analyzeSuccess + analyzeFailed));
        data.setSystemApps(new PercentagePair(systemApps, analyzeSuccess));
        data.setInstallLocation(installLocation);
        data.setTargetSdk(targetSdk);
        data.setMinSdk(minSdk);

        data.setAppSource(appSource);

        data.setApkSize(new MathStatistics(apkSize));

        data.setSignAlgorithm(signAlgorithm);

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
        if (appData == null) {
            analyzeFailed++;
            return;
        }
        analyzeSuccess++;
        if (appData.isSystemApp()) systemApps++;
        addToMap(installLocation, InstallLocationHelper.resolveInstallLocation(appData.getInstallLocation()), appData.getPackageName());
        addToMap(targetSdk, appData.getTargetSdk(), appData.getPackageName());
        addToMap(minSdk, appData.getMinSdk(), appData.getPackageName());
        apkSize[analyzeSuccess] = appData.getApkSize();
        addToMap(signAlgorithm, appData.getSignAlgorithm(), appData.getPackageName());
        addToMap(appSource, appData.getAppSource(), appData.getPackageName());

        activities[analyzeSuccess] = appData.getActivities();
        services[analyzeSuccess] = appData.getServices();
        providers[analyzeSuccess] = appData.getProviders();
        receivers[analyzeSuccess] = appData.getReceivers();

        usedPermissions[analyzeSuccess] = appData.getUsedPermissions();
        definedPermissions[analyzeSuccess] = appData.getDefinedPermissions();

        files[analyzeSuccess] = appData.getFiles();

        drawables[analyzeSuccess] = appData.getDrawables();
        differentDrawables[analyzeSuccess] = appData.getDifferentDrawables();

        layouts[analyzeSuccess] = appData.getLayouts();
        differentLayouts[analyzeSuccess] = appData.getDifferentLayouts();
    }

    private <T> void addToMap(Map<T, List<String>> map, T key, String packageName) {
        List<String> apps;
        if ((apps = map.get(key)) != null) {
            apps.add(packageName);
        } else {
            apps = new ArrayList<>();
            apps.add(packageName);
        }
        map.put(key, apps);
    }
}
