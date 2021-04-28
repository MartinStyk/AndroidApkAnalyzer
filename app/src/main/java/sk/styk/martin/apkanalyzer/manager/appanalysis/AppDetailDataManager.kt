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
        private val appGeneralDataManager: AppGeneralDataManager,
        private val certificateManager: CertificateManager,
        private val appComponentsManager: AppComponentsManager,
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
        return AppDetailData(
                analysisMode = analysisMode,
                generalData = appGeneralDataManager.get(packageInfo, analysisMode),
                certificateData = certificateManager.get(packageInfo),
                activityData = appComponentsManager.getActivities(packageInfo),
                serviceData = appComponentsManager.getServices(packageInfo),
                contentProviderData = appComponentsManager.getContentProviders(packageInfo),
                broadcastReceiverData = appComponentsManager.getBroadcastReceivers(packageInfo),
                permissionData = appPermissionManager.get(packageInfo),
                featureData = featuresManager.get(packageInfo),
        )
    }

    private fun PackageManager.getPackageArchiveInfoWithCorrectPath(pathToPackage: String, analysisFlags: Int): PackageInfo? {
        val packageInfo = getPackageArchiveInfo(pathToPackage, analysisFlags)
        packageInfo?.applicationInfo?.sourceDir = pathToPackage

        return packageInfo
    }
}