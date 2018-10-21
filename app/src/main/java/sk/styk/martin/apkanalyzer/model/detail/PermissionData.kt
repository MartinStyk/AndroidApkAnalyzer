package sk.styk.martin.apkanalyzer.model.detail

import android.annotation.SuppressLint
import android.content.pm.PermissionInfo
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import sk.styk.martin.apkanalyzer.business.analysis.logic.PermissionsService

/**
 * Single permission entry
 *
 * @author Martin Styk
 * @version 22.06.2017.
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class PermissionData(val name: String,
                          val simpleName: String = PermissionsService.createSimpleName(name),
                          val groupName: String? = null,
                          val protectionLevel: Int = PermissionInfo.PROTECTION_NORMAL) : Parcelable