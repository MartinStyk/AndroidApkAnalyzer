package sk.styk.martin.apkanalyzer.ui.activity.applist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sk.styk.martin.apkanalyzer.model.list.AppListData
import sk.styk.martin.apkanalyzer.util.coroutines.DispatcherProvider

internal const val LOADING_STATE = 0
internal const val EMPTY_STATE = 1
internal const val DATA_STATE = 2

abstract class BaseAppListViewModel constructor(
        val adapter: AppListAdapter,
        protected val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    protected open var appListData = listOf<AppListData>()
        set(value) {
            field = value
            adapter.data = value
            viewStateLiveData.value = when {
                value.isEmpty() -> EMPTY_STATE
                else -> DATA_STATE
            }
        }

    protected val viewStateLiveData = MutableLiveData(LOADING_STATE)
    val viewState: LiveData<Int> = viewStateLiveData

    val appClicked by lazy { adapter.appClicked }

}