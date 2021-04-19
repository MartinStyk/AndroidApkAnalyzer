package sk.styk.martin.apkanalyzer.ui.permission.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import sk.styk.martin.apkanalyzer.manager.appanalysis.AppPermissionManager
import sk.styk.martin.apkanalyzer.manager.navigationdrawer.NavigationDrawerModel
import sk.styk.martin.apkanalyzer.util.coroutines.DispatcherProvider
import javax.inject.Inject

@HiltViewModel
class PermissionListFragmentViewModel @Inject constructor(
        private val dispatcherProvider: DispatcherProvider,
        val permissionListAdapter: PermissionListAdapter,
        private val navigationDrawerModel: NavigationDrawerModel,
        private val appPermissionManager: AppPermissionManager,
) : ViewModel() {

    val openPermission = permissionListAdapter.openPermission

    private val isLoadingLiveData = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = isLoadingLiveData

    private val loadingProgressLiveData = MutableLiveData<Int>()
    val loadingProgress: LiveData<Int> = loadingProgressLiveData

    private val loadingProgressMaxLiveData = MutableLiveData<Int>()
    val loadingProgressMax: LiveData<Int> = loadingProgressMaxLiveData

    init {
        viewModelScope.launch {
            appPermissionManager.observeAllPermissionData()
                    .flowOn(dispatcherProvider.default())
                    .collect {
                        when (it) {
                            is AppPermissionManager.PermissionLoadingStatus.Loading -> {
                                loadingProgressLiveData.value = it.currentProgress
                                loadingProgressMaxLiveData.value = it.totalProgress
                                isLoadingLiveData.value = true
                            }
                            is AppPermissionManager.PermissionLoadingStatus.Data -> {
                                permissionListAdapter.permissions = it.localPermissionData
                                isLoadingLiveData.value = false
                            }
                        }
                    }
        }
    }

    fun onNavigationClick() = viewModelScope.launch {
        navigationDrawerModel.openDrawer()
    }

}
