package sk.styk.martin.apkanalyzer.core.apppermissions;

import kotlinx.coroutines.flow.flow
import sk.styk.martin.apkanalyzer.core.appanalysis.AppPermissionManager
import sk.styk.martin.apkanalyzer.core.appanalysis.PermissionStatus
import sk.styk.martin.apkanalyzer.core.appanalysis.model.PermissionData
import sk.styk.martin.apkanalyzer.core.appanalysis.model.UsedPermissionData
import sk.styk.martin.apkanalyzer.core.applist.InstalledAppsRepository
import sk.styk.martin.apkanalyzer.core.apppermissions.model.LocalPermissionData
import java.util.TreeSet
import javax.inject.Inject

class LocalPermissionManager @Inject constructor(
    private val appPermissionManager: AppPermissionManager,
    private val installedAppsRepository: InstalledAppsRepository,
) {

    sealed class PermissionLoadingStatus {
        data class Loading(val currentProgress: Int, val totalProgress: Int) : PermissionLoadingStatus()
        data class Data(val localPermissionData: List<LocalPermissionData>) : PermissionLoadingStatus()
    }

    suspend fun observeAllPermissionData() = flow {
        val allApps = installedAppsRepository.getAll()
        emit(PermissionLoadingStatus.Loading(0, allApps.size))

        val builder = LocalPermissionDataBuilder()

        allApps.forEachIndexed { index, appListData ->
            val packageName = appListData.packageName
            builder.addAll(packageName, appPermissionManager.getUsedPermissions(packageName))
            emit(PermissionLoadingStatus.Loading(index, allApps.size))
        }

        emit(PermissionLoadingStatus.Data(builder.build()))
    }

    internal class LocalPermissionDataBuilder {

        private val data = HashMap<PermissionData, MutableList<PermissionStatus>>()

        fun addAll(packageName: String, usedPermissionData: List<UsedPermissionData>?) {
            if (usedPermissionData == null) {
                return
            }

            for (data in usedPermissionData) {
                add(packageName, data)
            }
        }

        private fun add(packageName: String, usedPermissionData: UsedPermissionData) {
            var packageNamesForGivenPermissions: MutableList<PermissionStatus>? = data[usedPermissionData.permissionData]

            if (packageNamesForGivenPermissions == null) {
                packageNamesForGivenPermissions = ArrayList()
                data[usedPermissionData.permissionData] = packageNamesForGivenPermissions
            }
            packageNamesForGivenPermissions.add(PermissionStatus(packageName, usedPermissionData.isGranted))
        }

        fun build(): List<LocalPermissionData> {
            val sortedSet = TreeSet<Map.Entry<PermissionData, List<PermissionStatus>>> { entry1, entry2 -> entry2.value.size - entry1.value.size }
            sortedSet.addAll(data.entries)

            return sortedSet.mapTo(ArrayList(sortedSet.size)) { LocalPermissionData(it.key, it.value) }
        }
    }

}