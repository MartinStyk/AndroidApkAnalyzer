package sk.styk.martin.apkanalyzer.ui.appdetail.page.activity

import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import sk.styk.martin.apkanalyzer.manager.clipboard.ClipBoardManager
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.ui.appdetail.AppDetailFragmentViewModel
import sk.styk.martin.apkanalyzer.ui.appdetail.page.AppDetailPageFragmentViewModel

class AppActivityDetailFragmentViewModel @AssistedInject constructor(
        @Assisted appDetailFragmentViewModel: AppDetailFragmentViewModel,
        val activityAdapter: AppActivityDetailListAdapter,
        clipBoardManager: ClipBoardManager,
) : AppDetailPageFragmentViewModel(appDetailFragmentViewModel, activityAdapter, clipBoardManager) {

    val runActivity = activityAdapter.runActivity

    override fun onDataReceived(appDetailData: AppDetailData) : Boolean {
        activityAdapter.items = appDetailData.activityData
        return appDetailData.activityData.isNotEmpty()
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(appDetailFragmentViewModel: AppDetailFragmentViewModel): AppActivityDetailFragmentViewModel
    }
}