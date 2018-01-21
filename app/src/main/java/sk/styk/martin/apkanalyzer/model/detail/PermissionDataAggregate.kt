package sk.styk.martin.apkanalyzer.model.detail

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * @author Martin Styk
 * @version 22.06.2017.
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class PermissionDataAggregate(
        private val definesPermissions: List<PermissionData>,
        private val usesPermissions: List<UsedPermissionData>) : Parcelable {

    val usesPermissionsNames: List<String>
        get() = usesPermissions.mapTo(ArrayList<String>()) { it.permissionData.name }

    val definesPermissionsNames: List<String>
        get() = definesPermissions.mapTo(ArrayList<String>()) { it.name }
}