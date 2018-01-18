package sk.styk.martin.apkanalyzer.business.service

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import sk.styk.martin.apkanalyzer.model.detail.AppSource
import sk.styk.martin.apkanalyzer.model.detail.GeneralData
import sk.styk.martin.apkanalyzer.util.AndroidVersionHelper
import sk.styk.martin.apkanalyzer.util.InstallLocationHelper
import java.io.File

/**
 * @author Martin Styk
 * @version 30.06.2017.
 */
class GeneralDataService {

    fun get(packageInfo: PackageInfo, packageManager: PackageManager): GeneralData {

        val applicationInfo = packageInfo.applicationInfo

        val generalData = GeneralData()

        generalData.packageName = packageInfo.packageName
        generalData.versionCode = packageInfo.versionCode
        generalData.versionName = packageInfo.versionName
        generalData.installLocation = InstallLocationHelper.resolveInstallLocation(packageInfo.installLocation)
        generalData.firstInstallTime = packageInfo.firstInstallTime
        generalData.lastUpdateTime = packageInfo.lastUpdateTime

        applicationInfo?.let {

            generalData.icon = applicationInfo.loadIcon(packageManager)
            generalData.description = applicationInfo.loadDescription(packageManager)?.toString()
            generalData.applicationName = applicationInfo.loadLabel(packageManager)?.toString() ?: applicationInfo.packageName
            generalData.processName = applicationInfo.processName
            generalData.isSystemApp = (applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
            generalData.uid = applicationInfo.uid
            generalData.apkDirectory = applicationInfo.sourceDir
            generalData.apkSize = getApkSize(applicationInfo.sourceDir)
            generalData.dataDirectory = applicationInfo.dataDir
            generalData.minSdkVersion = AndroidManifestService.getMinSdkVersion(applicationInfo, packageManager)
            generalData.minSdkLabel = AndroidVersionHelper.resolveVersion(generalData.minSdkVersion)
            generalData.targetSdkVersion = applicationInfo.targetSdkVersion
            generalData.targetSdkLabel = AndroidVersionHelper.resolveVersion(applicationInfo.targetSdkVersion)

        }

        generalData.source = AppSource.get(packageManager, packageInfo.packageName, generalData.isSystemApp)

        return generalData
    }

    fun getApkSize(sourceDir: String): Long {
        return File(sourceDir).length()
    }

}
