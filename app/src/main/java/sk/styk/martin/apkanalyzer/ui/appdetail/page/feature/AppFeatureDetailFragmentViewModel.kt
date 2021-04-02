package sk.styk.martin.apkanalyzer.ui.appdetail.page.feature

import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import sk.styk.martin.apkanalyzer.manager.clipboard.ClipBoardManager
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.ui.appdetail.AppDetailFragmentViewModel
import sk.styk.martin.apkanalyzer.ui.appdetail.page.AppDetailPageFragmentViewModel

class AppFeatureDetailFragmentViewModel @AssistedInject constructor(
        @Assisted appDetailFragmentViewModel: AppDetailFragmentViewModel,
        private val featureAdapter: AppFeatureDetailListAdapter,
        clipBoardManager: ClipBoardManager,
) : AppDetailPageFragmentViewModel(appDetailFragmentViewModel, featureAdapter, clipBoardManager) {

    override fun onDataReceived(appDetailData: AppDetailData) {
        featureAdapter.items = appDetailData.featureData
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(appDetailFragmentViewModel: AppDetailFragmentViewModel): AppFeatureDetailFragmentViewModel
    }
}