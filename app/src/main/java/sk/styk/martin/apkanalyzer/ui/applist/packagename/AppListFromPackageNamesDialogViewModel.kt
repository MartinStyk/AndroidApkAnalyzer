package sk.styk.martin.apkanalyzer.ui.applist.packagename

import androidx.lifecycle.viewModelScope
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sk.styk.martin.apkanalyzer.manager.appanalysis.InstalledAppsManager
import sk.styk.martin.apkanalyzer.ui.applist.AppListAdapter
import sk.styk.martin.apkanalyzer.ui.applist.BaseAppListViewModel
import sk.styk.martin.apkanalyzer.util.coroutines.DispatcherProvider

class AppListFromPackageNamesDialogViewModel @AssistedInject constructor(
        @Assisted packageNames: List<String>,
        private val installedAppsManager: InstalledAppsManager,
        private val dispatcherProvider: DispatcherProvider,
        appListAdapter: AppListAdapter,
) : BaseAppListViewModel(appListAdapter) {

    init {
        viewModelScope.launch {
            appListData = withContext(dispatcherProvider.default()) {
                installedAppsManager.getForPackageNames(packageNames)
            }
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(packageNames: List<String>): AppListFromPackageNamesDialogViewModel
    }

}