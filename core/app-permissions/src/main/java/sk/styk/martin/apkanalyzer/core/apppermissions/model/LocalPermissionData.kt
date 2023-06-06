package sk.styk.martin.apkanalyzer.core.apppermissions.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import sk.styk.martin.apkanalyzer.core.appanalysis.PermissionStatus
import sk.styk.martin.apkanalyzer.core.appanalysis.model.PermissionData

@SuppressLint("ParcelCreator")
@Parcelize
data class LocalPermissionData(
    val permissionData: PermissionData,
    val permissionStatusList: List<PermissionStatus>,
) : Parcelable {

    val grantedPackageNames: List<String>
        get() = permissionStatusList.filter { it.isGranted }.mapTo(ArrayList()) { it.packageName }

    val notGrantedPackageNames: List<String>
        get() = permissionStatusList.filter { !it.isGranted }.mapTo(ArrayList()) { it.packageName }
}
