package sk.styk.martin.apkanalyzer.ui.activity.applist.searchable

import android.arch.lifecycle.MutableLiveData
import android.os.AsyncTask
import sk.styk.martin.apkanalyzer.business.analysis.livedata.AppListLiveData
import sk.styk.martin.apkanalyzer.model.detail.AppSource
import sk.styk.martin.apkanalyzer.model.list.AppListData


/**
 * @author Martin Styk
 * @date 23.12.2018.
 */
object AppListFilter {

    fun performFiltering(
            input: AppListLiveData,
            output: MutableLiveData<List<AppListData>?>,
            filterComponent: FilterComponent
    ) {

        AsyncTask.execute {
            val filtered = input.value?.filter {
                matchesNameFilter(it, filterComponent) && matchesSourceFilter(it, filterComponent)
            }

            output.postValue(filtered)
        }
    }

    private fun matchesNameFilter(appListData: AppListData, filterComponent: FilterComponent): Boolean {
        return filterComponent.name == null ||
                matchesNameFilter(appListData.applicationName.toLowerCase(), filterComponent) ||
                matchesNameFilter(appListData.packageName.toLowerCase(), filterComponent)
    }

    private fun matchesNameFilter(valueText: String, filterComponent: FilterComponent): Boolean {
        filterComponent.name?.let { name ->
            if (valueText.startsWith(name)) {
                return true
            } else {
                val words = valueText.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (word in words) {
                    if (word.startsWith(name)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun matchesSourceFilter(appListData: AppListData, filterComponent: FilterComponent): Boolean {
        return filterComponent.source == null || filterComponent.source?.run { equals(appListData.source) } ?: false
    }

    class FilterComponent {
        var name: String? = null
            set(value) {
                field = value?.toLowerCase()
            }

        var source: AppSource? = null
    }
}