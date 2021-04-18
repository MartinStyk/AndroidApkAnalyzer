package sk.styk.martin.apkanalyzer.manager.appanalysis

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.flow
import sk.styk.martin.apkanalyzer.model.statistics.LocalStatisticsAppData
import sk.styk.martin.apkanalyzer.model.statistics.LocalStatisticsData
import sk.styk.martin.apkanalyzer.model.statistics.LocalStatisticsDataBuilder
import javax.inject.Inject

@SuppressLint("PackageManagerGetSignatures")
private const val ANALYSIS_FLAGS = PackageManager.GET_SIGNATURES or
        PackageManager.GET_ACTIVITIES or
        PackageManager.GET_SERVICES or
        PackageManager.GET_PROVIDERS or
        PackageManager.GET_RECEIVERS or
        PackageManager.GET_PERMISSIONS

@WorkerThread
class LocalApplicationStatisticManager @Inject constructor(
        private val packageManager: PackageManager,
        private val installedAppsManager: InstalledAppsManager,
        private val generalDataService: GeneralDataService,
        private val fileService: FileDataService,
        private val resourceService: ResourceService,
        private val certificateService: CertificateService,
) {

    sealed class StatisticsLoadingStatus {
        data class Loading(val currentProgress: Int, val totalProgress: Int) : StatisticsLoadingStatus()
        data class Data(val data: LocalStatisticsData) : StatisticsLoadingStatus()
    }

    suspend fun loadStatisticsData() = flow<StatisticsLoadingStatus> {
        val allApps = installedAppsManager.getAll()
        emit(StatisticsLoadingStatus.Loading(0, allApps.size))

        val statsBuilder = LocalStatisticsDataBuilder(allApps.size)

        allApps.forEachIndexed { index, appListData ->
            statsBuilder.add(get(appListData.applicationName))
            emit(StatisticsLoadingStatus.Loading(index, allApps.size))
        }

        emit(StatisticsLoadingStatus.Data(statsBuilder.build()))
    }

    fun get(packageName: String): LocalStatisticsAppData? {

        val packageInfo = try {
            packageManager.getPackageInfo(packageName, ANALYSIS_FLAGS)
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
                minSdk = AndroidManifestManager.getMinSdkVersion(applicationInfo, packageManager)
                        ?: 0,
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
