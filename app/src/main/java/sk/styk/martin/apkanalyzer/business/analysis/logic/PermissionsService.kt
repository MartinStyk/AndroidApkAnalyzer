package sk.styk.martin.apkanalyzer.business.analysis.logic

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import sk.styk.martin.apkanalyzer.model.detail.PermissionData
import sk.styk.martin.apkanalyzer.model.detail.PermissionDataAggregate
import sk.styk.martin.apkanalyzer.model.detail.UsedPermissionData

/**
 * @author Martin Styk
 * @version 30.06.2017.
 */
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
        val requestedPermissionFlags =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    packageInfo.requestedPermissionsFlags
                else
                    IntArray(0)


        val requestedPermissions: MutableList<UsedPermissionData> = ArrayList(requestedPermissionNames.size)

        requestedPermissionNames.indices.forEach { index ->

            val name = requestedPermissionNames[index]
            val isGranted = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) && (requestedPermissionFlags[index] and PackageInfo.REQUESTED_PERMISSION_GRANTED != 0)

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

}


