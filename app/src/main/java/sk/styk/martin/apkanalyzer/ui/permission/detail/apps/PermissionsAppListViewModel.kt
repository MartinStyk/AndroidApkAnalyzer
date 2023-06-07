package sk.styk.martin.apkanalyzer.ui.permission.detail.apps

import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sk.styk.martin.apkanalyzer.core.applist.InstalledAppsRepository
import sk.styk.martin.apkanalyzer.core.common.coroutines.DispatcherProvider
import sk.styk.martin.apkanalyzer.ui.applist.AppListAdapter
import sk.styk.martin.apkanalyzer.ui.applist.BaseAppListViewModel
import sk.styk.martin.apkanalyzer.ui.permission.detail.pager.PermissionDetailFragmentViewModel

class PermissionsAppListViewModel @AssistedInject constructor(
    @Assisted private val permissionDetailFragmentViewModel: PermissionDetailFragmentViewModel,
    @Assisted private val showGranted: Boolean,
    private val installedAppsRepository: InstalledAppsRepository,
    private val dispatcherProvider: DispatcherProvider,
    adapter: AppListAdapter,
) : BaseAppListViewModel(adapter) {

    init {
        viewModelScope.launch(dispatcherProvider.default()) {
            val packageNames = if (showGranted) {
                permissionDetailFragmentViewModel.localPermissionData.grantedPackageNames
            } else {
                permissionDetailFragmentViewModel.localPermissionData.notGrantedPackageNames
            }
            val installedApps = installedAppsRepository.getForPackageNames(packageNames)

            withContext(dispatcherProvider.main()) {
                lazyAppListData = installedApps
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            permissionDetailFragmentViewModel: PermissionDetailFragmentViewModel,
            showGranted: Boolean,
        ): PermissionsAppListViewModel
    }
}
