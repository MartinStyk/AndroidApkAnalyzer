package sk.styk.martin.apkanalyzer.model.server;

import java.util.ArrayList;
import java.util.List;

import sk.styk.martin.apkanalyzer.model.detail.ActivityData;
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData;
import sk.styk.martin.apkanalyzer.model.detail.AppSource;
import sk.styk.martin.apkanalyzer.model.detail.BroadcastReceiverData;
import sk.styk.martin.apkanalyzer.model.detail.CertificateData;
import sk.styk.martin.apkanalyzer.model.detail.ClassPathData;
import sk.styk.martin.apkanalyzer.model.detail.ContentProviderData;
import sk.styk.martin.apkanalyzer.model.detail.FeatureData;
import sk.styk.martin.apkanalyzer.model.detail.FileData;
import sk.styk.martin.apkanalyzer.model.detail.GeneralData;
import sk.styk.martin.apkanalyzer.model.detail.ResourceData;
import sk.styk.martin.apkanalyzer.model.detail.ServiceData;
import sk.styk.martin.apkanalyzer.util.HashCodeHelper;

/**
 * Data sent to server for analysis.
 * <p>
 * Created by Martin Styk on 06.11.2017.
 */
public class ServerSideAppData {

    // ID of device which uploaded this data
    private String androidId;

    // Hash of data structure, can be used to identify two exactly same apps
    private int appHash;

    private AppDetailData.AnalysisMode analysisMode;

    // GeneralData
    private String packageName;
    private String applicationName;
    private String versionName;
    private int versionCode;
    private AppSource source;
    private long apkSize;
    private int minSdkVersion;
    private int targetSdkVersion;

    // CertificateData
    private String signAlgorithm;
    private String publicKeyMd5;
    private String certMd5;
    private int serialNumber;

    // Activities
    private int numberActivities;
    private int activitiesAggregatedHash;

    // Services
    private int numberServices;
    private int servicesAggregatedHash;

    // Content Providers
    private int numberContentProviders;
    private int providersAggregatedHash;

    // Broadcast Receivers
    private int numberBroadcastReceivers;
    private int receiversAggregatedHash;

    // Defined permissions
    private int numberDefinedPermissions;
    private int definedPermissionsAggregatedHash;

    // Used permissions
    private int numberUsedPermissions;
    private int usedPermissionsAggregatedHash;
    private List<String> permissions;

    // Features
    private int numberFeatures;
    private int featuresAggregatedHash;

    // FileData
    private String dexHash;
    private String arscHash;
    private String manifestHash;
    private List<String> pngHashes;
    private List<String> layoutHashes;

    private int numberDrawables;
    private int numberLayouts;
    private int numberMenus;
    private int numberFilesTotal;

    private int numberPngs;
    private int numberXmls;
    private int numberPngsWithDifferentName;
    private int numberXmlsWithDifferentName;


    // single combined appHash of all files in category
    private int pngsAggregatedHash;
    private int layoutsAggregatedHash;
    private int menusAggregatedHash;

    //ResourceData
    private int numberDifferentDrawables;
    private int numberDifferentLayouts;
    private int pngDrawables;
    private int ninePatchDrawables;
    private int jpgDrawables;
    private int gifDrawables;
    private int xmlDrawables;
    private int ldpiDrawables;
    private int mdpiDrawables;
    private int hdpiDrawables;
    private int xhdpiDrawables;
    private int xxhdpiDrawables;
    private int xxxhdpiDrawables;
    private int nodpiDrawables;
    private int tvdpiDrawables;
    private int unspecifiedDpiDrawables;

    // combined appHash of all classes
    private int classesAggregatedHash;
    private int totalNumberOfClasses;
    private int totalNumberOfClassesWithoutInnerClasses;

    public ServerSideAppData(AppDetailData appDetailData, String deviceId) {

        androidId = deviceId;
        this.appHash = appHash;

        analysisMode = appDetailData.getAnalysisMode();

        // GeneralData
        GeneralData generalData = appDetailData.getGeneralData();
        packageName = generalData.getPackageName();
        applicationName = generalData.getApplicationName();
        versionName = generalData.getVersionName();
        versionCode = generalData.getVersionCode();
        source = generalData.getSource();
        apkSize = generalData.getApkSize();
        minSdkVersion = generalData.getMinSdkVersion();
        targetSdkVersion = generalData.getTargetSdkVersion();

        // CertificateData
        CertificateData certificateData = appDetailData.getCertificateData();
        signAlgorithm = certificateData.getSignAlgorithm();
        publicKeyMd5 = certificateData.getPublicKeyMd5();
        certMd5 = certificateData.getCertMd5();
        serialNumber = certificateData.getSerialNumber();

        // Activities
        List<ActivityData> activityData = appDetailData.getActivityData();
        numberActivities = activityData.size();
        activitiesAggregatedHash = HashCodeHelper.hashList(activityData);

        // Services
        List<ServiceData> serviceData = appDetailData.getServiceData();
        numberServices = serviceData.size();
        servicesAggregatedHash = HashCodeHelper.hashList(serviceData);

        // Content Providers
        List<ContentProviderData> providerData = appDetailData.getContentProviderData();
        numberContentProviders = providerData.size();
        providersAggregatedHash = HashCodeHelper.hashList(providerData);

        // Broadcast Receivers
        List<BroadcastReceiverData> receiverData = appDetailData.getBroadcastReceiverData();
        numberBroadcastReceivers = receiverData.size();
        receiversAggregatedHash = HashCodeHelper.hashList(receiverData);

        // Defined permissions
        List<String> definedPermissions = appDetailData.getPermissionData().getDefinesPermissions();
        definedPermissionsAggregatedHash = HashCodeHelper.hashList(definedPermissions);
        numberDefinedPermissions = definedPermissions.size();

        // Used permissions
        permissions = appDetailData.getPermissionData().getUsesPermissions();
        usedPermissionsAggregatedHash = HashCodeHelper.hashList(permissions);
        numberUsedPermissions = permissions.size();

        // Features
        List<FeatureData> featureData = appDetailData.getFeatureData();
        numberFeatures = featureData.size();
        featuresAggregatedHash = HashCodeHelper.hashList(featureData);

        // FileData
        FileData fileData = appDetailData.getFileData();
        dexHash = fileData.getDexHash();
        arscHash = fileData.getArscHash();
        manifestHash = fileData.getManifestHash();

        pngHashes = fileData.getOnlyHash(fileData.getPngHashes());
        layoutHashes = fileData.getOnlyHash(fileData.getLayoutHashes());

        numberDrawables = fileData.getDrawableHashes().size();
        numberLayouts = layoutHashes.size();
        numberMenus = fileData.getOnlyHash(fileData.getMenuHashes()).size();
        numberFilesTotal = fileData.getTotalFiles();

        numberPngs = pngHashes.size();
        numberXmls = fileData.getNumberXmls();
        numberPngsWithDifferentName = fileData.getNumberPngsWithDifferentName();
        numberXmlsWithDifferentName = fileData.getNumberXmlsWithDifferentName();

        pngsAggregatedHash = HashCodeHelper.hashList(pngHashes);
        layoutsAggregatedHash = HashCodeHelper.hashList(layoutHashes);
        menusAggregatedHash = HashCodeHelper.hashList(fileData.getOnlyHash(fileData.getMenuHashes()));

        //ResourceData
        ResourceData resourceData = appDetailData.getResourceData();
        numberDifferentDrawables = resourceData.getDifferentDrawables();
        numberDifferentLayouts = resourceData.getDifferentLayouts();
        pngDrawables = resourceData.getPngDrawables();
        ninePatchDrawables = resourceData.getNinePatchDrawables();
        jpgDrawables = resourceData.getJpgDrawables();
        gifDrawables = resourceData.getGifDrawables();
        xmlDrawables = resourceData.getXmlDrawables();
        ldpiDrawables = resourceData.getLdpiDrawables();
        mdpiDrawables = resourceData.getMdpiDrawables();
        hdpiDrawables = resourceData.getHdpiDrawables();
        xhdpiDrawables = resourceData.getXhdpiDrawables();
        xxhdpiDrawables = resourceData.getXxhdpiDrawables();
        xxxhdpiDrawables = resourceData.getXxxhdpiDrawables();
        nodpiDrawables = resourceData.getNodpiDrawables();
        tvdpiDrawables = resourceData.getTvdpiDrawables();
        unspecifiedDpiDrawables = resourceData.getUnspecifiedDpiDrawables();

        // ClassPathData
        ClassPathData classPathData = appDetailData.getClassPathData();

        totalNumberOfClasses = classPathData.getPackageClasses().size() + classPathData.getOtherClasses().size();
        classesAggregatedHash = HashCodeHelper.hashList(classPathData.getPackageClasses(), classPathData.getOtherClasses());
        totalNumberOfClassesWithoutInnerClasses = totalNumberOfClasses - classPathData.getNumberOfInnerClasses();

        appHash = computeOverallHash();
    }

    private int computeOverallHash() {
        int result = 0;
        result = 31 * result + (packageName != null ? packageName.hashCode() : 0);
        result = 31 * result + versionCode;
        result = 31 * result + (publicKeyMd5 != null ? publicKeyMd5.hashCode() : 0);
        result = 31 * result + (certMd5 != null ? certMd5.hashCode() : 0);
        result = 31 * result + (dexHash != null ? dexHash.hashCode() : 0);
        result = 31 * result + (arscHash != null ? arscHash.hashCode() : 0);
        result = 31 * result + (manifestHash != null ? manifestHash.hashCode() : 0);
        result = 31 * result + numberPngs;
        result = 31 * result + numberXmls;
        result = 31 * result + pngsAggregatedHash;

        return result;
    }
}

