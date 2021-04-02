package sk.styk.martin.apkanalyzer.ui.appdetail.page.service

import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import sk.styk.martin.apkanalyzer.manager.clipboard.ClipBoardManager
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.ui.appdetail.AppDetailFragmentViewModel
import sk.styk.martin.apkanalyzer.ui.appdetail.page.AppDetailPageFragmentViewModel

class AppServiceDetailFragmentViewModel @AssistedInject constructor(
        @Assisted appDetailFragmentViewModel: AppDetailFragmentViewModel,
        private val serviceAdapter: AppServiceDetailListAdapter,
        clipBoardManager: ClipBoardManager,
) : AppDetailPageFragmentViewModel(appDetailFragmentViewModel, serviceAdapter, clipBoardManager) {

    override fun onDataReceived(appDetailData: AppDetailData) : Boolean {
        serviceAdapter.items = appDetailData.serviceData
        return appDetailData.serviceData.isNotEmpty()
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(appDetailFragmentViewModel: AppDetailFragmentViewModel): AppServiceDetailFragmentViewModel
    }
}