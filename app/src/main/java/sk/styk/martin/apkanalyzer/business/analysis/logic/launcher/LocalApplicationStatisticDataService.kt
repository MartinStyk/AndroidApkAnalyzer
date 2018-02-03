package sk.styk.martin.apkanalyzer.business.analysis.logic.launcher

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import sk.styk.martin.apkanalyzer.business.analysis.logic.AndroidManifestService
import sk.styk.martin.apkanalyzer.business.analysis.logic.CertificateService
import sk.styk.martin.apkanalyzer.business.analysis.logic.FileDataService
import sk.styk.martin.apkanalyzer.business.analysis.logic.GeneralDataService
import sk.styk.martin.apkanalyzer.business.analysis.logic.ResourceService
import sk.styk.martin.apkanalyzer.model.statistics.LocalStatisticsAppData

/**
 * @author Martin Styk
 * @version 28.07.2017.
 */
class LocalApplicationStatisticDataService(private val packageManager: PackageManager) {

    private val analysisFlags = PackageManager.GET_SIGNATURES or
            PackageManager.GET_ACTIVITIES or
            PackageManager.GET_SERVICES or
            PackageManager.GET_PROVIDERS or
            PackageManager.GET_RECEIVERS or
            PackageManager.GET_PERMISSIONS


    private val generalDataService: GeneralDataService = GeneralDataService()
    private val certificateService: CertificateService = CertificateService()
    private val fileService: FileDataService = FileDataService()
    private val resourceService: ResourceService = ResourceService()

    fun get(packageName: String): LocalStatisticsAppData? {

        val packageInfo = try {
            packageManager.getPackageInfo(packageName, analysisFlags)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        val applicationInfo = packageInfo.applicationInfo ?: return null

        val isSystemApp = (applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
        val fileData = fileService.get(packageInfo)
        val resourceData = resourceService.get(fileData)

        return LocalStatisticsAppData(
                packageName = packageName,
                isSystemApp = isSystemApp,
                installLocation = packageInfo.installLocation,
                targetSdk = applicationInfo.targetSdkVersion,
                minSdk = AndroidManifestService.getMinSdkVersion(applicationInfo, packageManager) ?: 0,
                apkSize = if (applicationInfo.sourceDir != null) generalDataService.computeApkSize(applicationInfo.sourceDir) else 0,
                appSource = GeneralDataService.getAppSource(packageManager, packageName, isSystemApp),
                signAlgorithm = certificateService.getSignAlgorithm(packageInfo) ?: "Unknown",
                activities = packageInfo.activities?.size ?: 0,
                services = packageInfo.services?.size ?: 0,
                providers = packageInfo.providers?.size ?: 0,
                receivers = packageInfo.receivers?.size ?: 0,
                definedPermissions = packageInfo.permissions?.size ?: 0,
                usedPermissions = packageInfo.requestedPermissions?.size ?: 0,
                files = fileData.totalFiles,
                drawables = resourceData.drawables,
                differentDrawables = resourceData.differentDrawables,
                layouts = resourceData.layouts,
                differentLayouts = resourceData.differentLayouts
        )
    }

}
