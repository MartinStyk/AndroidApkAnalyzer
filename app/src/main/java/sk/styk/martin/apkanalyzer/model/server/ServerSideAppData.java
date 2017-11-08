package sk.styk.martin.apkanalyzer.model.server;

import java.math.BigInteger;
import java.util.Date;
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
import sk.styk.martin.apkanalyzer.model.detail.FileEntry;
import sk.styk.martin.apkanalyzer.model.detail.GeneralData;
import sk.styk.martin.apkanalyzer.model.detail.ResourceData;
import sk.styk.martin.apkanalyzer.model.detail.ServiceData;

/**
 * Data sent to server for analysis.
 * <p>
 * Created by Martin Styk on 06.11.2017.
 */
public class ServerSideAppData {

    private String androidId;

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
    private Date startDate;
    private Date endDate;
    private String publicKeyMd5;
    private String certMd5;
    private BigInteger serialNumber;
    private String issuerName;
    private String issuerOrganization;
    private String issuerCountry;
    private String subjectName;
    private String subjectOrganization;
    private String subjectCountry;

    // Activities
    private int numberActivities;
    private List<ActivityData> activityData;

    // Services
    private int numberServices;
    private List<ServiceData> serviceData;

    // Content Providers
    private int numberContentProviders;
    private List<ContentProviderData> contentProviderData;

    // Broadcast Receivers
    private int numberBroadcastReceivers;
    private List<BroadcastReceiverData> broadcastReceiverData;

    // Defined permissions
    private int numberDefinedPermissions;
    private List<String> definedPermissions;

    // Used permissions
    private int numberUsedPermissions;
    private List<String> usedPermissions;

    // Features
    private int numberFeatures;
    private List<FeatureData> featureData;

    // FileData
    private String dexHash;
    private String arscHash;
    private List<FileEntry> drawableHashes;
    private List<FileEntry> layoutHashes;
    private List<FileEntry> assetHashes;
    private List<FileEntry> otherHashes;

    private int numberDrawables;
    private int numberLayouts;
    private int numberAssets;
    private int numberOthers;

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

    // ClassPathData
    private List<String> packageClasses;
    private int numberPackageClasses;
    private int numberOtherClasses;

    public ServerSideAppData(AppDetailData appDetailData, String deviceId) {

        androidId = deviceId;

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
        startDate = certificateData.getStartDate();
        endDate = certificateData.getStartDate();
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
        activityData = appDetailData.getActivityData();
        numberActivities = activityData.size();

        // Services
        serviceData = appDetailData.getServiceData();
        numberServices = serviceData.size();

        // Content Providers
        contentProviderData = appDetailData.getContentProviderData();
        numberContentProviders = contentProviderData.size();

        // Broadcast Receivers
        broadcastReceiverData = appDetailData.getBroadcastReceiverData();
        numberBroadcastReceivers = broadcastReceiverData.size();

        // Defined permissions
        definedPermissions = appDetailData.getPermissionData().getDefinesPermissions();
        numberDefinedPermissions = definedPermissions.size();

        // Used permissions
        usedPermissions = appDetailData.getPermissionData().getUsesPermissions();
        numberUsedPermissions = usedPermissions.size();

        // Features
        featureData = appDetailData.getFeatureData();
        numberFeatures = featureData.size();


        // FileData
        FileData fileData = appDetailData.getFileData();
        dexHash = fileData.getDexHash();
        arscHash = fileData.getArscHash();
        drawableHashes = fileData.getDrawableHashes();
        layoutHashes = fileData.getLayoutHashes();
        assetHashes = fileData.getAssetHashes();
        otherHashes = fileData.getOtherHashes();

        numberDrawables = drawableHashes.size();
        numberLayouts = layoutHashes.size();
        numberAssets = assetHashes.size();
        numberOthers = otherHashes.size();

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

        packageClasses = classPathData.getPackageClasses();

        numberPackageClasses = packageClasses.size();
        numberOtherClasses = classPathData.getOtherClasses().size();
    }
}
