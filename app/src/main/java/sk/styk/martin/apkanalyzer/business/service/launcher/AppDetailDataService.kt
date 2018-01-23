package sk.styk.martin.apkanalyzer.business.service.launcher

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Log
import sk.styk.martin.apkanalyzer.business.service.AppComponentsService
import sk.styk.martin.apkanalyzer.business.service.CertificateService
import sk.styk.martin.apkanalyzer.business.service.DexService
import sk.styk.martin.apkanalyzer.business.service.FeaturesService
import sk.styk.martin.apkanalyzer.business.service.FileDataService
import sk.styk.martin.apkanalyzer.business.service.GeneralDataService
import sk.styk.martin.apkanalyzer.business.service.PermissionsService
import sk.styk.martin.apkanalyzer.business.service.ResourceService
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData

/**
 * Retrieve apps installed on device
 *
 * @author Martin Styk
 * @version 14.06.2017.
 */
class AppDetailDataService(private val packageManager: PackageManager) {
    private val TAG = AppDetailDataService::class.java.simpleName

    private val analysisFlags = PackageManager.GET_SIGNATURES or
            PackageManager.GET_ACTIVITIES or
            PackageManager.GET_SERVICES or
            PackageManager.GET_PROVIDERS or
            PackageManager.GET_RECEIVERS or
            PackageManager.GET_PERMISSIONS or
            PackageManager.GET_CONFIGURATIONS

    private val generalDataService = GeneralDataService()
    private val certificateService = CertificateService()
    private val appComponentsService = AppComponentsService()
    private val permissionsService = PermissionsService()
    private val featuresService = FeaturesService()
    private val fileDataService = FileDataService()
    private val resourceService = ResourceService()
    private val dexService = DexService()

    fun get(packageName: String?, pathToPackage: String?): AppDetailData? {

        val packageInfo: PackageInfo
        val analysisMode: AppDetailData.AnalysisMode?
        try {
            // decide whether we analyze installed app or only apk file
            packageInfo = when {
                !packageName.isNullOrBlank() && pathToPackage.isNullOrBlank() -> {
                    analysisMode = AppDetailData.AnalysisMode.INSTALLED_PACKAGE
                    packageManager.getPackageInfo(packageName, analysisFlags)
                }
                packageName.isNullOrBlank() && !pathToPackage.isNullOrBlank() -> {
                    analysisMode = AppDetailData.AnalysisMode.APK_FILE
                    packageManager.getPackageArchiveInfoWithCorrectPath(pathToPackage!!, analysisFlags)
                }
                else -> throw IllegalArgumentException("At least one way to get package needs to be specified  [$packageName/$pathToPackage]")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Can not read package info of package [$packageName/$pathToPackage] :$e")
            return null
        }

        return try {
            val fileData = fileDataService.get(packageInfo)

            AppDetailData(
                    analysisMode = analysisMode,
                    generalData = generalDataService.get(packageInfo, packageManager),
                    certificateData = certificateService.get(packageInfo),
                    activityData = appComponentsService.getActivities(packageInfo, packageManager),
                    serviceData = appComponentsService.getServices(packageInfo),
                    contentProviderData = appComponentsService.getContentProviders(packageInfo),
                    broadcastReceiverData = appComponentsService.getBroadcastReceivers(packageInfo),
                    permissionData = permissionsService.get(packageInfo, packageManager),
                    featureData = featuresService.get(packageInfo),
                    fileData = fileData,
                    resourceData = resourceService.get(fileData),
                    classPathData = dexService.get(packageInfo)
            )

        } catch (e: Exception) {
            // we catch a general exception here, because some repackaged APKs are really naughty
            // and we rather show user error screen than app failure
            Log.e(TAG, e.toString())
            null

        }

    }

    private fun PackageManager.getPackageArchiveInfoWithCorrectPath(pathToPackage: String, analysisFlags: Int): PackageInfo {
        val packageInfo = getPackageArchiveInfo(pathToPackage, analysisFlags)
        packageInfo?.applicationInfo?.sourceDir = pathToPackage

        return packageInfo
    }
}

