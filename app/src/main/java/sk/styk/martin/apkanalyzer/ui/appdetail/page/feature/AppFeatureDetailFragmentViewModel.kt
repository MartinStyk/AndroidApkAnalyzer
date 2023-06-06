package sk.styk.martin.apkanalyzer.ui.appdetail.page.feature

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import sk.styk.martin.apkanalyzer.manager.clipboard.ClipBoardManager
import sk.styk.martin.apkanalyzer.core.appanalysis.model.AppDetailData
import sk.styk.martin.apkanalyzer.ui.appdetail.AppDetailFragmentViewModel
import sk.styk.martin.apkanalyzer.ui.appdetail.page.AppDetailPageFragmentViewModel

class AppFeatureDetailFragmentViewModel @AssistedInject constructor(
    @Assisted appDetailFragmentViewModel: AppDetailFragmentViewModel,
    private val featureAdapter: AppFeatureDetailListAdapter,
    clipBoardManager: ClipBoardManager,
) : AppDetailPageFragmentViewModel(appDetailFragmentViewModel, featureAdapter, clipBoardManager) {

    override fun onDataReceived(appDetailData: AppDetailData): Boolean {
        featureAdapter.items = appDetailData.featureData
        return appDetailData.featureData.isNotEmpty()
    }

    @AssistedFactory
    interface Factory {
        fun create(appDetailFragmentViewModel: AppDetailFragmentViewModel): AppFeatureDetailFragmentViewModel
    }
}
