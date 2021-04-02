package sk.styk.martin.apkanalyzer.ui.appdetail.page.provider

import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import sk.styk.martin.apkanalyzer.manager.clipboard.ClipBoardManager
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.ui.appdetail.AppDetailFragmentViewModel
import sk.styk.martin.apkanalyzer.ui.appdetail.page.AppDetailPageFragmentViewModel

class AppProviderDetailFragmentViewModel @AssistedInject constructor(
        @Assisted  appDetailFragmentViewModel: AppDetailFragmentViewModel,
        private val providerAdapter: AppProviderDetailListAdapter,
        clipBoardManager: ClipBoardManager,
) : AppDetailPageFragmentViewModel(appDetailFragmentViewModel, providerAdapter, clipBoardManager) {

    override fun onDataReceived(appDetailData: AppDetailData) {
        providerAdapter.items = appDetailData.contentProviderData
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(appDetailFragmentViewModel: AppDetailFragmentViewModel): AppProviderDetailFragmentViewModel
    }
}