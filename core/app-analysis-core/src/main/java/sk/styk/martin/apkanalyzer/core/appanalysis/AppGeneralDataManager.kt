package sk.styk.martin.apkanalyzer.core.appanalysis

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import sk.styk.martin.apkanalyzer.core.appanalysis.model.AppSource
import sk.styk.martin.apkanalyzer.core.appanalysis.model.GeneralData
import sk.styk.martin.apkanalyzer.core.appanalysis.model.InstallLocation
import java.io.File
import javax.inject.Inject

class AppGeneralDataManager @Inject constructor(
    private val packageManager: PackageManager,
    private val androidVersionManager: AndroidVersionManager,
    private val androidManifestManager: AndroidManifestManager,
    private val appInstallSourceManager: AppInstallSourceManager,
) {

    sealed interface AnalysisMode {
        object InstalledPackage : AnalysisMode
        object ApkFile : AnalysisMode
    }

    fun get(packageInfo: PackageInfo, analysisMode: AnalysisMode): GeneralData {
        val applicationInfo = packageInfo.applicationInfo

        val minSdk = getMinSdk(applicationInfo, analysisMode)

        return GeneralData(
            packageName = packageInfo.packageName,
            applicationName = applicationInfo.loadLabel(packageManager).toString(),
            processName = applicationInfo.processName,
            versionName = packageInfo.versionName,
            versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) packageInfo.longVersionCode else packageInfo.versionCode.toLong(),
            isSystemApp = appInstallSourceManager.isSystemInstalledApp(packageInfo),
            uid = applicationInfo.uid,
            description = applicationInfo.loadDescription(packageManager)?.toString(),
            apkDirectory = applicationInfo.sourceDir,
            dataDirectory = applicationInfo.dataDir,

            source = appInstallSourceManager.getAppInstallSource(packageInfo),
            appInstaller = appInstallSourceManager.appInstallingPackage(packageInfo),

            installLocation = InstallLocation.from(packageInfo.installLocation),
            apkSize = computeApkSize(applicationInfo.sourceDir),
            firstInstallTime = if (packageInfo.firstInstallTime > 0) packageInfo.firstInstallTime else null,
            lastUpdateTime = if (packageInfo.lastUpdateTime > 0) packageInfo.lastUpdateTime else null,

            minSdkVersion = minSdk,
            minSdkLabel = androidVersionManager.resolveVersion(minSdk),

            targetSdkVersion = applicationInfo.targetSdkVersion,
            targetSdkLabel = androidVersionManager.resolveVersion(applicationInfo.targetSdkVersion),

            icon = applicationInfo.loadIcon(packageManager),
        )
    }

    fun computeApkSize(sourceDir: String): Long = File(sourceDir).length()

    private fun getMinSdk(applicationInfo: ApplicationInfo, analysisMode: AnalysisMode): Int? =
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> applicationInfo.minSdkVersion
            analysisMode is AnalysisMode.InstalledPackage -> androidManifestManager.getMinSdkVersion(applicationInfo)
            else -> null
        }

}
