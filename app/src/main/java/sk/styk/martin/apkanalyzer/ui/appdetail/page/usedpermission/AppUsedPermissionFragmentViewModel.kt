package sk.styk.martin.apkanalyzer.ui.appdetail.page.usedpermission

import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import sk.styk.martin.apkanalyzer.manager.clipboard.ClipBoardManager
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.ui.appdetail.AppDetailFragmentViewModel
import sk.styk.martin.apkanalyzer.ui.appdetail.adapters.TextListAdapter
import sk.styk.martin.apkanalyzer.ui.appdetail.page.AppDetailPageFragmentViewModel
import sk.styk.martin.apkanalyzer.util.TextInfo

class AppUsedPermissionFragmentViewModel @AssistedInject constructor(
        @Assisted appDetailFragmentViewModel: AppDetailFragmentViewModel,
        private val textAdapter: TextListAdapter,
        clipBoardManager: ClipBoardManager
) : AppDetailPageFragmentViewModel(appDetailFragmentViewModel, textAdapter, clipBoardManager) {

    override fun onDataReceived(appDetailData: AppDetailData): Boolean {
        textAdapter.items = appDetailData.permissionData.usesPermissionsNames.map { TextInfo.from(it) }
        return appDetailData.permissionData.usesPermissionsNames.isNotEmpty()
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(appDetailFragmentViewModel: AppDetailFragmentViewModel): AppUsedPermissionFragmentViewModel
    }
}