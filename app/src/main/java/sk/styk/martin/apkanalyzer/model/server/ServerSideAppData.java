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
    private String issuerName;
    private String issuerOrganization;
    private String issuerCountry;
    private String subjectName;
    private String subjectOrganization;
    private String subjectCountry;

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
    private List<String> features;

    // FileData
    private String dexHash;
    private String arscHash;
    private String manifestHash;
    private List<String> pngHashes;
    private List<String> layoutHashes;
    private List<String> menuHashes;

    private int numberPngs;
    private int numberLayouts;
    private int numberAssets;
    private int numberTotal;

    // single combined appHash of all files in category
    private int pngsAggregatedHash;
    private int layoutsAggregatedHash;
    private int assetsAggregatedHash;
    private int otherAggregatedHash;

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

    // combined appHash of all pacakge classes
    private int packageClassesAggregatedHash;
    private int numberPackageClasses;
    // combined appHash of all other classes
    private int otherClassesAggregatedHash;
    private int numberOtherClasses;

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
        issuerName = certificateData.getIssuerName();
        issuerOrganization = certificateData.getIssuerOrganization();
        issuerCountry = certificateData.getIssuerCountry();
        subjectName = certificateData.getSubjectName();
        subjectOrganization = certificateData.getSubjectOrganization();
        subjectCountry = certificateData.getSubjectCountry();


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
        features = new ArrayList<>(featureData.size());
        for (FeatureData fData : featureData) {
            features.add(fData.getName());
        }

        // FileData
        FileData fileData = appDetailData.getFileData();
        dexHash = fileData.getDexHash();
        arscHash = fileData.getArscHash();
        manifestHash = fileData.getManifestHash();

        pngHashes = fileData.getOnlyHash(fileData.getPngHashes());
        layoutHashes = fileData.getOnlyHash(fileData.getLayoutHashes());
        menuHashes = fileData.getOnlyHash(fileData.getMenuHashes());

        numberPngs = pngHashes.size();
        numberLayouts = layoutHashes.size();
        numberAssets = menuHashes.size();

        pngsAggregatedHash = HashCodeHelper.hashList(pngHashes);
        layoutsAggregatedHash = HashCodeHelper.hashList(layoutHashes);
        assetsAggregatedHash = HashCodeHelper.hashList(menuHashes);

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

        numberPackageClasses = classPathData.getPackageClasses().size();
        packageClassesAggregatedHash = HashCodeHelper.hashList(classPathData.getPackageClasses());
        numberOtherClasses = classPathData.getOtherClasses().size();
        otherClassesAggregatedHash = HashCodeHelper.hashList(classPathData.getOtherClasses());

        appHash = computeOverallHash();
    }

    private int computeOverallHash() {
        int result = 0;
        result = 31 * result + (packageName != null ? packageName.hashCode() : 0);
        result = 31 * result + (applicationName != null ? applicationName.hashCode() : 0);
        result = 31 * result + (versionName != null ? versionName.hashCode() : 0);
        result = 31 * result + versionCode;
        result = 31 * result + (int) (apkSize ^ (apkSize >>> 32));
        result = 31 * result + minSdkVersion;
        result = 31 * result + targetSdkVersion;
        result = 31 * result + (publicKeyMd5 != null ? publicKeyMd5.hashCode() : 0);
        result = 31 * result + (certMd5 != null ? certMd5.hashCode() : 0);
        result = 31 * result + numberActivities;
        result = 31 * result + activitiesAggregatedHash;
        result = 31 * result + numberServices;
        result = 31 * result + servicesAggregatedHash;
        result = 31 * result + numberContentProviders;
        result = 31 * result + providersAggregatedHash;
        result = 31 * result + numberBroadcastReceivers;
        result = 31 * result + receiversAggregatedHash;
        result = 31 * result + numberDefinedPermissions;
        result = 31 * result + definedPermissionsAggregatedHash;
        result = 31 * result + numberUsedPermissions;
        result = 31 * result + usedPermissionsAggregatedHash;
        result = 31 * result + numberFeatures;
        result = 31 * result + featuresAggregatedHash;
        result = 31 * result + (dexHash != null ? dexHash.hashCode() : 0);
        result = 31 * result + (arscHash != null ? arscHash.hashCode() : 0);
        result = 31 * result + numberPngs;
        result = 31 * result + numberLayouts;
        result = 31 * result + numberAssets;
        result = 31 * result + pngsAggregatedHash;
        result = 31 * result + layoutsAggregatedHash;
        result = 31 * result + assetsAggregatedHash;
        result = 31 * result + otherAggregatedHash;
        result = 31 * result + numberDifferentDrawables;
        result = 31 * result + numberDifferentLayouts;
        result = 31 * result + pngDrawables;
        result = 31 * result + ninePatchDrawables;
        result = 31 * result + jpgDrawables;
        result = 31 * result + gifDrawables;
        result = 31 * result + xmlDrawables;
        result = 31 * result + ldpiDrawables;
        result = 31 * result + mdpiDrawables;
        result = 31 * result + hdpiDrawables;
        result = 31 * result + xhdpiDrawables;
        result = 31 * result + xxhdpiDrawables;
        result = 31 * result + xxxhdpiDrawables;
        result = 31 * result + nodpiDrawables;
        result = 31 * result + tvdpiDrawables;
        result = 31 * result + unspecifiedDpiDrawables;
        result = 31 * result + packageClassesAggregatedHash;
        result = 31 * result + numberPackageClasses;
        result = 31 * result + otherClassesAggregatedHash;
        result = 31 * result + numberOtherClasses;
        return result;
    }
}

