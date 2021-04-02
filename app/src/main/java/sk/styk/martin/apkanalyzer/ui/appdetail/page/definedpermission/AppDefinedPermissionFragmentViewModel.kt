package sk.styk.martin.apkanalyzer.ui.appdetail.page.usedpermission

import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import sk.styk.martin.apkanalyzer.manager.clipboard.ClipBoardManager
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.ui.appdetail.AppDetailFragmentViewModel
import sk.styk.martin.apkanalyzer.ui.appdetail.adapters.TextListAdapter
import sk.styk.martin.apkanalyzer.ui.appdetail.page.AppDetailPageFragmentViewModel
import sk.styk.martin.apkanalyzer.util.TextInfo

class AppDefinedPermissionFragmentViewModel @AssistedInject constructor(
        @Assisted appDetailFragmentViewModel: AppDetailFragmentViewModel,
        val textAdapter: TextListAdapter,
        clipBoardManager: ClipBoardManager,
) : AppDetailPageFragmentViewModel(appDetailFragmentViewModel, textAdapter, clipBoardManager) {

    override fun onDataReceived(appDetailData: AppDetailData) : Boolean {
        textAdapter.items = appDetailData.permissionData.definesPermissionsNames.map { TextInfo.from(it) }
        return textAdapter.items.isNotEmpty()
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(appDetailFragmentViewModel: AppDetailFragmentViewModel): AppDefinedPermissionFragmentViewModel
    }
}