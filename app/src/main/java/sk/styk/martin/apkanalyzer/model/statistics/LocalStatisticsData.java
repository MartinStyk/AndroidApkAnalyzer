package sk.styk.martin.apkanalyzer.model.statistics;

import java.util.Map;

import sk.styk.martin.apkanalyzer.util.MathStatistics;
import sk.styk.martin.apkanalyzer.util.PercentagePair;

/**
 * Created by Martin Styk on 28.07.2017.
 */

public class LocalStatisticsData {

    private int totalApplications;

    private PercentagePair systemApps;
    private Map<Integer, PercentagePair> installLocation;
    private Map<Integer, PercentagePair> targetSdk;
    private Map<Integer, PercentagePair> minSdk;
    private MathStatistics apkSize;

    private Map<String, PercentagePair> signAlgorithm;

    private MathStatistics activites;
    private MathStatistics services;
    private MathStatistics providers;
    private MathStatistics receivers;

    private MathStatistics usedPermissions;
    private MathStatistics definedPermissions;

    private MathStatistics files;

    private MathStatistics drawables;
    private MathStatistics differentDrawables;

    private MathStatistics layouts;
    private MathStatistics differentLayouts;

    public int getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(int totalApplications) {
        this.totalApplications = totalApplications;
    }

    public PercentagePair getSystemApps() {
        return systemApps;
    }

    public void setSystemApps(PercentagePair systemApps) {
        this.systemApps = systemApps;
    }

    public Map<Integer, PercentagePair> getInstallLocation() {
        return installLocation;
    }

    public void setInstallLocation(Map<Integer, PercentagePair> installLocation) {
        this.installLocation = installLocation;
    }

    public Map<Integer, PercentagePair> getTargetSdk() {
        return targetSdk;
    }

    public void setTargetSdk(Map<Integer, PercentagePair> targetSdk) {
        this.targetSdk = targetSdk;
    }

    public Map<Integer, PercentagePair> getMinSdk() {
        return minSdk;
    }

    public void setMinSdk(Map<Integer, PercentagePair> minSdk) {
        this.minSdk = minSdk;
    }

    public MathStatistics getApkSize() {
        return apkSize;
    }

    public void setApkSize(MathStatistics apkSize) {
        this.apkSize = apkSize;
    }

    public Map<String, PercentagePair> getSignAlgorithm() {
        return signAlgorithm;
    }

    public void setSignAlgorithm(Map<String, PercentagePair> signAlgorithm) {
        this.signAlgorithm = signAlgorithm;
    }

    public MathStatistics getActivites() {
        return activites;
    }

    public void setActivites(MathStatistics activites) {
        this.activites = activites;
    }

    public MathStatistics getServices() {
        return services;
    }

    public void setServices(MathStatistics services) {
        this.services = services;
    }

    public MathStatistics getProviders() {
        return providers;
    }

    public void setProviders(MathStatistics providers) {
        this.providers = providers;
    }

    public MathStatistics getReceivers() {
        return receivers;
    }

    public void setReceivers(MathStatistics receivers) {
        this.receivers = receivers;
    }

    public MathStatistics getUsedPermissions() {
        return usedPermissions;
    }

    public void setUsedPermissions(MathStatistics usedPermissions) {
        this.usedPermissions = usedPermissions;
    }

    public MathStatistics getDefinedPermissions() {
        return definedPermissions;
    }

    public void setDefinedPermissions(MathStatistics definedPermissions) {
        this.definedPermissions = definedPermissions;
    }

    public MathStatistics getFiles() {
        return files;
    }

    public void setFiles(MathStatistics files) {
        this.files = files;
    }

    public MathStatistics getDrawables() {
        return drawables;
    }

    public void setDrawables(MathStatistics drawables) {
        this.drawables = drawables;
    }

    public MathStatistics getDifferentDrawables() {
        return differentDrawables;
    }

    public void setDifferentDrawables(MathStatistics differentDrawables) {
        this.differentDrawables = differentDrawables;
    }

    public MathStatistics getLayouts() {
        return layouts;
    }

    public void setLayouts(MathStatistics layouts) {
        this.layouts = layouts;
    }

    public MathStatistics getDifferentLayouts() {
        return differentLayouts;
    }

    public void setDifferentLayouts(MathStatistics differentLayouts) {
        this.differentLayouts = differentLayouts;
    }
}
