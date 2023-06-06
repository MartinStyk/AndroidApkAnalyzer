package sk.styk.martin.apkanalyzer.core.appanalysis.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class PermissionDataAggregate(
    private val definesPermissions: List<PermissionData>,
    private val usesPermissions: List<UsedPermissionData>,
) : Parcelable {

    val usesPermissionsNames: List<String>
        get() = usesPermissions.mapTo(ArrayList()) { it.permissionData.name }

    val definesPermissionsNames: List<String>
        get() = definesPermissions.mapTo(ArrayList()) { it.name }
}
