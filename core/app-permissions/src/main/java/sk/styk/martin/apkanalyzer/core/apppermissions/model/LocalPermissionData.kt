package sk.styk.martin.apkanalyzer.core.apppermissions.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import sk.styk.martin.apkanalyzer.core.appanalysis.model.PermissionData

@Parcelize
data class LocalPermissionData(
    val permissionData: PermissionData,
    val permissionStatusList: List<LocalPermissionStatus>,
) : Parcelable {

    val grantedPackageNames: List<String> by lazy { permissionStatusList.filter { it.isGranted }.mapTo(ArrayList()) { it.packageName } }

    val notGrantedPackageNames: List<String> by lazy { permissionStatusList.filter { !it.isGranted }.mapTo(ArrayList()) { it.packageName } }
}
