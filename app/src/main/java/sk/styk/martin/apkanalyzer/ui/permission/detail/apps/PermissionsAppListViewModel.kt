package sk.styk.martin.apkanalyzer.ui.permission.detail.apps

import androidx.lifecycle.viewModelScope
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sk.styk.martin.apkanalyzer.manager.appanalysis.InstalledAppsManager
import sk.styk.martin.apkanalyzer.ui.applist.AppListAdapter
import sk.styk.martin.apkanalyzer.ui.applist.BaseAppListViewModel
import sk.styk.martin.apkanalyzer.ui.permission.detail.pager.PermissionDetailFragmentViewModel
import sk.styk.martin.apkanalyzer.util.coroutines.DispatcherProvider

class PermissionsAppListViewModel @AssistedInject constructor(
        @Assisted private val permissionDetailFragmentViewModel: PermissionDetailFragmentViewModel,
        @Assisted private val showGranted: Boolean,
        private val installedAppsManager: InstalledAppsManager,
        private val dispatcherProvider: DispatcherProvider,
        adapter: AppListAdapter
) : BaseAppListViewModel(adapter) {

    init {
        viewModelScope.launch(dispatcherProvider.default()) {
            val packageNames = if (showGranted) {
                permissionDetailFragmentViewModel.localPermissionData.grantedPackageNames
            } else {
                permissionDetailFragmentViewModel.localPermissionData.notGrantedPackageNames
            }
            val installedApps = installedAppsManager.getForPackageNames(packageNames)

            withContext(dispatcherProvider.main()) {
                appListData = installedApps
            }
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(permissionDetailFragmentViewModel: PermissionDetailFragmentViewModel,
                   showGranted: Boolean): PermissionsAppListViewModel
    }

}