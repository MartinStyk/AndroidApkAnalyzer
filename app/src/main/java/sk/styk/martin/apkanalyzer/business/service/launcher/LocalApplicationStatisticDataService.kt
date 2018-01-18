package sk.styk.martin.apkanalyzer.business.service.launcher

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import sk.styk.martin.apkanalyzer.business.service.AndroidManifestService
import sk.styk.martin.apkanalyzer.business.service.CertificateService
import sk.styk.martin.apkanalyzer.business.service.FileDataService
import sk.styk.martin.apkanalyzer.business.service.GeneralDataService
import sk.styk.martin.apkanalyzer.business.service.ResourceService
import sk.styk.martin.apkanalyzer.model.detail.AppSource
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
        val packageInfo: PackageInfo?

        try {
            packageInfo = packageManager.getPackageInfo(packageName, analysisFlags)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        val applicationInfo = packageInfo.applicationInfo ?: return null

        val data = LocalStatisticsAppData()
        data.packageName = packageName
        data.isSystemApp = (applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
        data.installLocation = packageInfo.installLocation
        data.targetSdk = applicationInfo.targetSdkVersion
        data.minSdk = AndroidManifestService.getMinSdkVersion(applicationInfo, packageManager)
        data.apkSize = if (applicationInfo.sourceDir != null) generalDataService.getApkSize(applicationInfo.sourceDir) else 0
        data.appSource = AppSource.get(packageManager, packageName, data.isSystemApp)

        data.signAlgorithm = certificateService.getSignAlgorithm(packageInfo)

        data.activities = if (packageInfo.activities == null) 0 else packageInfo.activities.size
        data.services = if (packageInfo.services == null) 0 else packageInfo.services.size
        data.providers = if (packageInfo.providers == null) 0 else packageInfo.providers.size
        data.receivers = if (packageInfo.receivers == null) 0 else packageInfo.receivers.size

        data.definedPermissions = if (packageInfo.permissions == null) 0 else packageInfo.permissions.size
        data.usedPermissions = if (packageInfo.requestedPermissions == null) 0 else packageInfo.requestedPermissions.size

        val fileData = fileService.get(packageInfo)
        data.files = fileData.menuHashes.size +
                fileData.drawableHashes.size +
                fileData.layoutHashes.size +
                fileData.otherHashes.size + 2 // +2 for arsc and dex

        val resourceData = resourceService.get(fileData)
        data.drawables = resourceData.drawables
        data.differentDrawables = resourceData.differentDrawables
        data.layouts = resourceData.layouts
        data.differentLayouts = resourceData.differentLayouts

        return data
    }

}
