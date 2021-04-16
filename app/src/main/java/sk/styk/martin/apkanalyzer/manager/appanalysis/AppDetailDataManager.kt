package sk.styk.martin.apkanalyzer.manager.appanalysis

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import sk.styk.martin.apkanalyzer.dependencyinjection.util.ForApplication
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import java.io.File
import javax.inject.Inject

class AppDetailDataManager @Inject constructor(
        @ForApplication private val context: Context,
        private val packageManager: PackageManager,
        private val appPermissionManager: AppPermissionManager,
        private val featuresManager: FeaturesManager,
        private val generalDataService: GeneralDataService,
        private val certificateService: CertificateService,
        private val fileDataService: FileDataService,
        private val appComponentsService: AppComponentsService,
        private val resourceService: ResourceService,
        private val dexService: DexService,
) {

    private val analysisFlags = PackageManager.GET_SIGNATURES or
            PackageManager.GET_ACTIVITIES or
            PackageManager.GET_SERVICES or
            PackageManager.GET_PROVIDERS or
            PackageManager.GET_RECEIVERS or
            PackageManager.GET_PERMISSIONS or
            PackageManager.GET_CONFIGURATIONS

    fun loadForInstalledPackage(packageName: String) = get(
            analysisMode = AppDetailData.AnalysisMode.INSTALLED_PACKAGE,
            packageInfo = packageManager.getPackageInfo(packageName, analysisFlags)
    )

    fun loadForExternalPackage(accessibleFile: File): AppDetailData {
        return get(
                analysisMode = AppDetailData.AnalysisMode.APK_FILE,
                packageInfo = packageManager.getPackageArchiveInfoWithCorrectPath(accessibleFile.absolutePath, analysisFlags)
                        ?: throw IllegalArgumentException()
        )
    }

    private fun get(analysisMode: AppDetailData.AnalysisMode, packageInfo: PackageInfo): AppDetailData {

        val fileData = fileDataService.get(packageInfo)

        return AppDetailData(
                analysisMode = analysisMode,
                generalData = generalDataService.get(packageInfo, analysisMode),
                certificateData = certificateService.get(packageInfo),
                activityData = appComponentsService.getActivities(packageInfo),
                serviceData = appComponentsService.getServices(packageInfo),
                contentProviderData = appComponentsService.getContentProviders(packageInfo),
                broadcastReceiverData = appComponentsService.getBroadcastReceivers(packageInfo),
                permissionData = appPermissionManager.get(packageInfo),
                featureData = featuresManager.get(packageInfo),
                fileData = fileData,
                resourceData = resourceService.get(fileData),
                classPathData = dexService.get(packageInfo)
        )
    }

    private fun PackageManager.getPackageArchiveInfoWithCorrectPath(pathToPackage: String, analysisFlags: Int): PackageInfo? {
        val packageInfo = getPackageArchiveInfo(pathToPackage, analysisFlags)
        packageInfo?.applicationInfo?.sourceDir = pathToPackage

        return packageInfo
    }
}