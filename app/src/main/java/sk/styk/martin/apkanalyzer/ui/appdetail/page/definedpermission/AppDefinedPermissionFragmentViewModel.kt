package sk.styk.martin.apkanalyzer.ui.appdetail.page.usedpermission

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.ui.appdetail.AppDetailFragmentViewModel
import sk.styk.martin.apkanalyzer.ui.appdetail.adapters.TextListAdapter
import sk.styk.martin.apkanalyzer.util.TextInfo

class AppDefinedPermissionFragmentViewModel @AssistedInject constructor(
        @Assisted private val appDetailFragmentViewModel: AppDetailFragmentViewModel,
        val textAdapter: TextListAdapter
) : ViewModel(), DefaultLifecycleObserver {

    private val appDetailsObserver = Observer<AppDetailData> {
        textAdapter.items = it.permissionData.definesPermissionsNames.map { TextInfo.from(it) }
    }

    init {
        appDetailFragmentViewModel.appDetails.observeForever(appDetailsObserver)
    }

    override fun onCleared() {
        super.onCleared()
        appDetailFragmentViewModel.appDetails.removeObserver(appDetailsObserver)
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(appDetailFragmentViewModel: AppDetailFragmentViewModel): AppDefinedPermissionFragmentViewModel
    }
}