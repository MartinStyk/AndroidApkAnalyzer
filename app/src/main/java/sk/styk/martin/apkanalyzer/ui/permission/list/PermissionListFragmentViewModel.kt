package sk.styk.martin.apkanalyzer.ui.permission.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import sk.styk.martin.apkanalyzer.core.appanalysis.AppPermissionManager
import sk.styk.martin.apkanalyzer.core.apppermissions.LocalPermissionManager
import sk.styk.martin.apkanalyzer.core.common.coroutines.DispatcherProvider
import sk.styk.martin.apkanalyzer.manager.navigationdrawer.NavigationDrawerModel
import javax.inject.Inject

@HiltViewModel
class PermissionListFragmentViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    val permissionListAdapter: PermissionListAdapter,
    private val navigationDrawerModel: NavigationDrawerModel,
    private val localPermissionManager: LocalPermissionManager,
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
            localPermissionManager.observeAllPermissionData()
                .flowOn(dispatcherProvider.default())
                .collect {
                    when (it) {
                        is LocalPermissionManager.PermissionLoadingStatus.Loading -> {
                            loadingProgressLiveData.value = it.currentProgress
                            loadingProgressMaxLiveData.value = it.totalProgress
                            isLoadingLiveData.value = true
                        }

                        is LocalPermissionManager.PermissionLoadingStatus.Data -> {
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
