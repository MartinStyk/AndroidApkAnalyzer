package sk.styk.martin.apkanalyzer.core.appstatistics

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.flow
import sk.styk.martin.apkanalyzer.core.appanalysis.AndroidManifestManager
import sk.styk.martin.apkanalyzer.core.appanalysis.AppGeneralDataManager
import sk.styk.martin.apkanalyzer.core.appanalysis.CertificateManager
import sk.styk.martin.apkanalyzer.core.applist.InstalledAppsRepository
import sk.styk.martin.apkanalyzer.core.appstatistics.model.StatisticsAppData
import sk.styk.martin.apkanalyzer.core.appstatistics.model.StatisticsData
import sk.styk.martin.apkanalyzer.core.appstatistics.model.StatisticsDataBuilder
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("PackageManagerGetSignatures")
private const val ANALYSIS_FLAGS = PackageManager.GET_SIGNATURES or
        PackageManager.GET_ACTIVITIES or
        PackageManager.GET_SERVICES or
        PackageManager.GET_PROVIDERS or
        PackageManager.GET_RECEIVERS or
        PackageManager.GET_PERMISSIONS

@WorkerThread
class LocalApplicationStatisticManager @Inject internal constructor(
    private val packageManager: PackageManager,
    private val installedAppsRepository: InstalledAppsRepository,
    private val generalDataService: AppGeneralDataManager,
    private val certificateService: CertificateManager,
    private val androidManifestManager: AndroidManifestManager,
) {

    sealed class StatisticsLoadingStatus {
        data class Loading(val currentProgress: Int, val totalProgress: Int) : StatisticsLoadingStatus()
        data class Data(val data: StatisticsData) : StatisticsLoadingStatus()
    }

    suspend fun loadStatisticsData() = flow {
        val allApps = installedAppsRepository.getAll()
        emit(StatisticsLoadingStatus.Loading(0, allApps.size))

        val statsBuilder = StatisticsDataBuilder(allApps.size)

        allApps.forEachIndexed { index, appListData ->
            statsBuilder.add(get(appListData.packageName))
            emit(StatisticsLoadingStatus.Loading(index, allApps.size))
        }

        emit(StatisticsLoadingStatus.Data(statsBuilder.build()))
    }

    fun get(packageName: String): StatisticsAppData? {
        val packageInfo = try {
            packageManager.getPackageInfo(packageName, ANALYSIS_FLAGS)
        } catch (e: Exception) {
            Timber.tag("AppStatistics").w(e, "Package info for statistics failed. Package name= $packageName")
            return null
        }

        val applicationInfo = packageInfo.applicationInfo ?: return null

        val isSystemApp = (applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0

        return StatisticsAppData(
            packageName = packageName,
            isSystemApp = isSystemApp,
            installLocation = packageInfo.installLocation,
            targetSdk = applicationInfo.targetSdkVersion,
            minSdk = androidManifestManager.getMinSdkVersion(applicationInfo) ?: 0,
            apkSize = if (applicationInfo.sourceDir != null) generalDataService.computeApkSize(applicationInfo.sourceDir) else 0,
            appSource = AppGeneralDataManager.getAppSource(
                packageManager,
                packageName,
                isSystemApp
            ),
            signAlgorithm = certificateService.getSignAlgorithm(packageInfo) ?: "Unknown",
            activities = packageInfo.activities?.size ?: 0,
            services = packageInfo.services?.size ?: 0,
            providers = packageInfo.providers?.size ?: 0,
            receivers = packageInfo.receivers?.size ?: 0,
            definedPermissions = packageInfo.permissions?.size ?: 0,
            usedPermissions = packageInfo.requestedPermissions?.size ?: 0,
        )
    }
}
