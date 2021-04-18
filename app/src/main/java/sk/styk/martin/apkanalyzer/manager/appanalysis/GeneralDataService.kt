package sk.styk.martin.apkanalyzer.manager.appanalysis

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.model.detail.AppSource
import sk.styk.martin.apkanalyzer.model.detail.GeneralData
import sk.styk.martin.apkanalyzer.util.AndroidVersionManager
import sk.styk.martin.apkanalyzer.util.InstallLocationHelper
import java.io.File
import javax.inject.Inject

class GeneralDataService @Inject constructor(
        private val packageManager: PackageManager,
        private val androidVersionManager: AndroidVersionManager,
) {

    fun get(packageInfo: PackageInfo, analysisMode: AppDetailData.AnalysisMode): GeneralData {

        val applicationInfo = packageInfo.applicationInfo

        val isSystemApp = (applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
        val appInstaller = findAppInstaller(packageInfo.packageName, packageManager)

        val minSdk = getMinSdk(applicationInfo, packageManager, analysisMode)

        return GeneralData(
                packageName = packageInfo.packageName,
                applicationName = applicationInfo.loadLabel(packageManager).toString(),
                processName = applicationInfo.processName,
                versionName = packageInfo.versionName,
                versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) packageInfo.longVersionCode else packageInfo.versionCode.toLong(),
                isSystemApp = isSystemApp,
                uid = applicationInfo.uid,
                description = applicationInfo.loadDescription(packageManager)?.toString(),
                apkDirectory = applicationInfo.sourceDir,
                dataDirectory = applicationInfo.dataDir,

                source = getAppSource(packageManager, packageInfo.packageName, isSystemApp),
                appInstaller = appInstaller,

                installLocation = InstallLocationHelper.resolveInstallLocation(packageInfo.installLocation),
                apkSize = computeApkSize(applicationInfo.sourceDir),
                firstInstallTime = if (packageInfo.firstInstallTime > 0) packageInfo.firstInstallTime else null,
                lastUpdateTime = if (packageInfo.lastUpdateTime > 0) packageInfo.lastUpdateTime else null,

                minSdkVersion = minSdk,
                minSdkLabel = androidVersionManager.resolveVersion(minSdk),

                targetSdkVersion = applicationInfo.targetSdkVersion,
                targetSdkLabel = androidVersionManager.resolveVersion(applicationInfo.targetSdkVersion),

                icon = applicationInfo.loadIcon(packageManager)
        )
    }

    fun computeApkSize(sourceDir: String): Long = File(sourceDir).length()

    private fun getMinSdk(applicationInfo: ApplicationInfo, packageManager: PackageManager, analysisMode: AppDetailData.AnalysisMode): Int? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                applicationInfo.minSdkVersion
            else if (analysisMode.toString() == AppDetailData.AnalysisMode.INSTALLED_PACKAGE.toString())
                AndroidManifestManager.getMinSdkVersion(applicationInfo, packageManager)
            else null

    companion object {
        fun getAppSource(packageManager: PackageManager, packageName: String, isSystem: Boolean): AppSource {
            val installer = findAppInstaller(packageName, packageManager)

            return if (installer == AppSource.GOOGLE_PLAY.installerPackageName) AppSource.GOOGLE_PLAY
            else if (installer == AppSource.AMAZON_STORE.installerPackageName) AppSource.AMAZON_STORE
            else if (installer == AppSource.SYSTEM_PREINSTALED.installerPackageName || isSystem) AppSource.SYSTEM_PREINSTALED
            else AppSource.UNKNOWN
        }

        fun findAppInstaller(packageName: String, packageManager: PackageManager): String? {
            return try {
                packageManager.getInstallerPackageName(packageName)
            } catch (e: Exception) {
                null //this means package is not installed
            }
        }
    }
}
