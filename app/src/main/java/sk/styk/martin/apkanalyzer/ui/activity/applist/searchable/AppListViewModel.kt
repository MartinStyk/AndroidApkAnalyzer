package sk.styk.martin.apkanalyzer.ui.activity.applist.searchable

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import sk.styk.martin.apkanalyzer.business.analysis.livedata.AppListLiveData
import sk.styk.martin.apkanalyzer.model.detail.AppSource
import sk.styk.martin.apkanalyzer.model.list.AppListData
import sk.styk.martin.appshare.util.live.SingleLiveEvent

/**
 * @author Martin Styk {@literal <martin.styk@gmail.com>}
 */
class AppListViewModel(application: Application) : AndroidViewModel(application), AppListAdapter.AppListClickListener {

    val appListData by lazy {
        MediatorLiveData<List<AppListData>?>().apply {
            addSource(allApps) { value -> setValue(value) }
            addSource(filteredApps) { value -> setValue(value) }
        }
    }

    private val allApps by lazy { AppListLiveData(getApplication()) }

    private val filteredApps = MutableLiveData<List<AppListData>?>()

    val isLoading by lazy { MutableLiveData<Boolean>().apply { value = true } }

    val isEmpty by lazy { MutableLiveData<Boolean>().apply { value = false } }

    val adapter by lazy { AppListAdapter(this) }

    val appClicked = SingleLiveEvent<AppListData>()

    val isFilterActive by lazy { MutableLiveData<Boolean>().apply { value = false } }

    val filterComponent by lazy { AppListFilter.FilterComponent() }

    fun dataChanged(data: List<AppListData>?) {
        if (data != null) {
            isLoading.value = false
            isEmpty.value = data.isEmpty()
            adapter.data = data
        }
    }

    override fun onAppClick(appListData: AppListData) {
        appClicked.value = appListData
    }

    fun filterOnAppName(appName: String?) {
        AppListFilter.performFiltering(allApps, filteredApps, filterComponent.also { it.name = appName })
        isFilterActive.value = filterComponent.isFilteringActive()
    }

    fun filterOnAppSource(appSource: AppSource?) {
        AppListFilter.performFiltering(allApps, filteredApps, filterComponent.also { it.source = appSource })
        isFilterActive.value = filterComponent.isFilteringActive()
    }

    override fun onCleared() {
        allApps.dispose()
        super.onCleared()
    }
}