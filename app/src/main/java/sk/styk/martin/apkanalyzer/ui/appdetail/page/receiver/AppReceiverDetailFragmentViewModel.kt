package sk.styk.martin.apkanalyzer.ui.appdetail.page.receiver

import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import sk.styk.martin.apkanalyzer.manager.clipboard.ClipBoardManager
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.ui.appdetail.AppDetailFragmentViewModel
import sk.styk.martin.apkanalyzer.ui.appdetail.page.AppDetailPageFragmentViewModel
import sk.styk.martin.apkanalyzer.ui.appdetail.page.provider.AppReceiverDetailListAdapter

class AppReceiverDetailFragmentViewModel @AssistedInject constructor(
        @Assisted  appDetailFragmentViewModel: AppDetailFragmentViewModel,
        private val receiverAdapter: AppReceiverDetailListAdapter,
        clipBoardManager: ClipBoardManager,
) : AppDetailPageFragmentViewModel(appDetailFragmentViewModel, receiverAdapter, clipBoardManager) {

    override fun onDataReceived(appDetailData: AppDetailData) {
        receiverAdapter.items = appDetailData.broadcastReceiverData
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(appDetailFragmentViewModel: AppDetailFragmentViewModel): AppReceiverDetailFragmentViewModel
    }

}