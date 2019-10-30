package sk.styk.martin.apkanalyzer.business.analysis.logic

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.annotation.WorkerThread
import sk.styk.martin.apkanalyzer.model.detail.PermissionData
import sk.styk.martin.apkanalyzer.model.detail.PermissionDataAggregate
import sk.styk.martin.apkanalyzer.model.detail.UsedPermissionData

@WorkerThread
class PermissionsService {

    fun get(packageInfo: PackageInfo, packageManager: PackageManager): PermissionDataAggregate {

        val definedPermissions = getDefinedPermissions(packageInfo)
        val requestedPermissions = getUsedPermissions(packageInfo, packageManager)

        return PermissionDataAggregate(definedPermissions, requestedPermissions)
    }

    private fun getDefinedPermissions(packageInfo: PackageInfo): List<PermissionData> {
        val permissionInfos = packageInfo.permissions ?: return ArrayList(0)

        return permissionInfos.mapTo(ArrayList<PermissionData>(permissionInfos.size)) {
            PermissionData(name = it.name, groupName = it.group, protectionLevel = it.protectionLevel)
        }
    }

    fun getUsedPermissions(packageInfo: PackageInfo, packageManager: PackageManager): List<UsedPermissionData> {

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


