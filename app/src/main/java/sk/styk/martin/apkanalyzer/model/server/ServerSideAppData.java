package sk.styk.martin.apkanalyzer.model.server;

import java.math.BigInteger;
import java.util.ArrayList;
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
    private List<String> activityNames;

    // Services
    private int numberServices;
    private List<String> serviceNames;

    // Content Providers
    private int numberContentProviders;
    private List<String> contentProviderNames;

    // Broadcast Receivers
    private int numberBroadcastReceivers;
    private List<String> broadcastReceiverNames;

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
    private List<String> drawableHashes;
    private List<String> layoutHashes;
    private List<String> assetHashes;
    private List<String> otherHashes;

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
        List<ActivityData> activityData = appDetailData.getActivityData();
        numberActivities = activityData.size();
        activityNames = new ArrayList<>(activityData.size());
        for (ActivityData aData : activityData) {
            activityNames.add(aData.getName());
        }


        // Services
        List<ServiceData> serviceData = appDetailData.getServiceData();
        numberServices = serviceData.size();
        serviceNames = new ArrayList<>(serviceData.size());
        for (ServiceData sData : serviceData) {
            serviceNames.add(sData.getName());
        }

        // Content Providers
        List<ContentProviderData> providerData = appDetailData.getContentProviderData();
        numberContentProviders = providerData.size();
        contentProviderNames = new ArrayList<>(providerData.size());
        for (ContentProviderData cData : providerData) {
            contentProviderNames.add(cData.getName());
        }


        // Broadcast Receivers
        List<BroadcastReceiverData> receiverData = appDetailData.getBroadcastReceiverData();
        numberBroadcastReceivers = receiverData.size();
        broadcastReceiverNames = new ArrayList<>(receiverData.size());
        for (BroadcastReceiverData rData : receiverData) {
            broadcastReceiverNames.add(rData.getName());
        }

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

        drawableHashes = fileData.getOnlyHash(fileData.getDrawableHashes());
        layoutHashes = fileData.getOnlyHash(fileData.getLayoutHashes());
        assetHashes = fileData.getOnlyHash(fileData.getAssetHashes());
        otherHashes = fileData.getOnlyHash(fileData.getOtherHashes());

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
