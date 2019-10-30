package sk.styk.martin.apkanalyzer.business.analysis.logic.launcher

import android.content.pm.PackageManager
import androidx.annotation.WorkerThread
import sk.styk.martin.apkanalyzer.business.analysis.logic.PermissionsService
import sk.styk.martin.apkanalyzer.model.detail.UsedPermissionData

@WorkerThread
class LocalPermissionsDataService(private val packageManager: PackageManager) {

    private val permissionsService: PermissionsService = PermissionsService()

    fun get(packageName: String): List<UsedPermissionData>? {
        return try {
            val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
            permissionsService.getUsedPermissions(packageInfo, packageManager)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}
