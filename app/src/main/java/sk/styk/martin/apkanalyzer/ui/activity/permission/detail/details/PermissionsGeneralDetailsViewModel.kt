package sk.styk.martin.apkanalyzer.ui.activity.permission.detail.details

import androidx.lifecycle.ViewModel
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import sk.styk.martin.apkanalyzer.ui.activity.permission.detail.pager.PermissionDetailFragmentViewModel

class PermissionsGeneralDetailsViewModel @AssistedInject constructor(
        @Assisted private val permissionDetailFragmentViewModel: PermissionDetailFragmentViewModel,
) : ViewModel() {

    val permissionData = permissionDetailFragmentViewModel.localPermissionData.permissionData
    val grantedApps = permissionDetailFragmentViewModel.localPermissionData.grantedPackageNames.size
    val notGrantedApps = permissionDetailFragmentViewModel.localPermissionData.notGrantedPackageNames.size

    @AssistedInject.Factory
    interface Factory {
        fun create(permissionDetailFragmentViewModel: PermissionDetailFragmentViewModel): PermissionsGeneralDetailsViewModel
    }
}