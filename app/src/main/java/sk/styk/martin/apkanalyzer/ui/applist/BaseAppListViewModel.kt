package sk.styk.martin.apkanalyzer.ui.applist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import sk.styk.martin.apkanalyzer.core.applist.model.LazyAppListData

internal const val LOADING_STATE = 0
internal const val EMPTY_STATE = 1
internal const val DATA_STATE = 2

abstract class BaseAppListViewModel constructor(
    val adapter: AppListAdapter,
) : ViewModel() {

    protected open var lazyAppListData = listOf<LazyAppListData>()
        set(value) {
            field = value
            adapter.data = value
            viewStateLiveData.value = when {
                value.isEmpty() -> EMPTY_STATE
                else -> DATA_STATE
            }
        }

    protected val viewStateLiveData = MutableLiveData(LOADING_STATE)
    val viewState: LiveData<Int> = viewStateLiveData.distinctUntilChanged()

    val appClicked by lazy { adapter.appClicked }
}
