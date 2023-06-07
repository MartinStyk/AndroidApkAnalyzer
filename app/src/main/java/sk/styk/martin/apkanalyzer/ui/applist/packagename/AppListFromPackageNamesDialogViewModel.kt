package sk.styk.martin.apkanalyzer.ui.applist.packagename

import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sk.styk.martin.apkanalyzer.core.applist.InstalledAppsRepository
import sk.styk.martin.apkanalyzer.ui.applist.AppListAdapter
import sk.styk.martin.apkanalyzer.ui.applist.BaseAppListViewModel
import sk.styk.martin.apkanalyzer.core.common.coroutines.DispatcherProvider

class AppListFromPackageNamesDialogViewModel @AssistedInject constructor(
    @Assisted packageNames: List<String>,
    private val installedAppsRepository: InstalledAppsRepository,
    private val dispatcherProvider: DispatcherProvider,
    appListAdapter: AppListAdapter,
) : BaseAppListViewModel(appListAdapter) {

    init {
        viewModelScope.launch {
            lazyAppListData = withContext(dispatcherProvider.default()) {
                installedAppsRepository.getForPackageNames(packageNames)
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(packageNames: List<String>): AppListFromPackageNamesDialogViewModel
    }
}
