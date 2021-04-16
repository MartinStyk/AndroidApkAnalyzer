package sk.styk.martin.apkanalyzer.manager.appanalysis

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import kotlinx.coroutines.flow.flow
import sk.styk.martin.apkanalyzer.model.detail.PermissionData
import sk.styk.martin.apkanalyzer.model.detail.PermissionDataAggregate
import sk.styk.martin.apkanalyzer.model.detail.UsedPermissionData
import sk.styk.martin.apkanalyzer.model.permissions.LocalPermissionData
import sk.styk.martin.apkanalyzer.model.permissions.LocalPermissionDataBuilder
import javax.inject.Inject

class AppPermissionManager @Inject constructor(private val packageManager: PackageManager,
                                               private val installedAppsManager: InstalledAppsManager) {

    sealed class PermissionLoadingStatus {
        data class Loading(val currentProgress: Int, val totalProgress: Int) : PermissionLoadingStatus()
        data class Data(val localPermissionData: List<LocalPermissionData>) : PermissionLoadingStatus()
    }

    suspend fun observeAllPermissionData() = flow<PermissionLoadingStatus> {
        val allApps = installedAppsManager.getAll()
        emit(PermissionLoadingStatus.Loading(0, allApps.size))

        val builder = LocalPermissionDataBuilder()

        fun getDetail(packageName: String): List<UsedPermissionData>? = try {
            val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
            getUsedPermissions(packageInfo)
        } catch (e: Exception) {
            null
        }

        allApps.forEachIndexed { index, appListData ->
            val packageName = appListData.packageName
            builder.addAll(packageName, getDetail(packageName))
            emit(PermissionLoadingStatus.Loading(index, allApps.size))
        }

        emit(PermissionLoadingStatus.Data(builder.build()))
    }


    fun get(packageInfo: PackageInfo): PermissionDataAggregate {
        val definedPermissions = getDefinedPermissions(packageInfo)
        val requestedPermissions = getUsedPermissions(packageInfo)

        return PermissionDataAggregate(definedPermissions, requestedPermissions)
    }

    private fun getDefinedPermissions(packageInfo: PackageInfo): List<PermissionData> {
        val permissionInfos = packageInfo.permissions ?: return ArrayList(0)

        return permissionInfos.mapTo(ArrayList<PermissionData>(permissionInfos.size)) {
            PermissionData(name = it.name, groupName = it.group, protectionLevel = it.protectionLevel)
        }
    }

    private fun getUsedPermissions(packageInfo: PackageInfo): List<UsedPermissionData> {

        val requestedPermissionNames = packageInfo.requestedPermissions ?: return emptyList()
        val requestedPermissionFlags = packageInfo.requestedPermissionsFlags
        val requestedPermissions: MutableList<UsedPermissionData> = ArrayList(requestedPermissionNames.size)

        requestedPermissionNames.indices.forEach { index ->

            val name = requestedPermissionNames[index]
            val isGranted = (requestedPermissionFlags[index] and PackageInfo.REQUESTED_PERMISSION_GRANTED != 0)

            val permissionData =
                    try {
                        val permissionInfo = packageManager.getPermissionInfo(name, PackageManager.GET_META_DATA)
                        PermissionData(name = name, groupName = permissionInfo.group, protectionLevel = permissionInfo.protectionLevel)
                    } catch (e: Exception) {
                        // we failed to get permission data from package manager. Try to use things we know
                        PermissionData(name = name)
                    }

            requestedPermissions.add(UsedPermissionData(permissionData, isGranted))
        }
        return requestedPermissions
    }

    companion object {
        fun createSimpleName(name: String): String {
            var simpleNameBuilder = StringBuilder(name)

            val lastDot = name.lastIndexOf(".")
            if (lastDot > 0 && lastDot < name.length)
                simpleNameBuilder = StringBuilder(name.substring(lastDot + 1))

            var i = 0
            var previousSpace = false
            while (++i < simpleNameBuilder.length) {
                if (simpleNameBuilder[i] == '_') {
                    simpleNameBuilder.replace(i, i + 1, " ")
                    previousSpace = true
                } else {
                    if (!previousSpace) {
                        val lowercase = Character.toLowerCase(simpleNameBuilder[i])
                        simpleNameBuilder.replace(i, i + 1, lowercase.toString())
                    }
                    previousSpace = false
                }
            }
            return simpleNameBuilder.toString()
        }
    }
}


