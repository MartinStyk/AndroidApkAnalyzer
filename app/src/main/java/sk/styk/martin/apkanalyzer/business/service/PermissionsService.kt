package sk.styk.martin.apkanalyzer.business.service

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import sk.styk.martin.apkanalyzer.model.detail.PermissionData
import sk.styk.martin.apkanalyzer.model.detail.PermissionDataAgregate
import sk.styk.martin.apkanalyzer.model.detail.UsedPermissionData

/**
 * @author Martin Styk
 * @version 30.06.2017.
 */
class PermissionsService {

    fun get(packageInfo: PackageInfo, packageManager: PackageManager): PermissionDataAgregate {

        val definedPermissions = getDefinedPermissions(packageInfo)
        val requestedPermissions = getUsedPermissions(packageInfo, packageManager)

        return PermissionDataAgregate(definedPermissions, requestedPermissions)
    }

    private fun getDefinedPermissions(packageInfo: PackageInfo): List<PermissionData> {
        val permissionInfos = packageInfo.permissions ?: return emptyList()

        return permissionInfos.mapTo(ArrayList<PermissionData>(permissionInfos.size)) {
            PermissionData(it.name, it.group, it.protectionLevel)
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
                        PermissionData(name, permissionInfo.group, permissionInfo.protectionLevel)
                    } catch (e: Exception) {
                        // we failed to get permission data from package manager. Try to use things we know
                        PermissionData(name, null, Integer.MIN_VALUE)
                    }

            requestedPermissions.add(UsedPermissionData(permissionData, isGranted))
        }
        return requestedPermissions

    }

}


