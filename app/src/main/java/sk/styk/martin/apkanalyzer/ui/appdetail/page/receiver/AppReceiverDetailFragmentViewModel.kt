package sk.styk.martin.apkanalyzer.ui.appdetail.page.receiver

import androidx.lifecycle.LifecycleOwner
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import sk.styk.martin.apkanalyzer.manager.clipboard.ClipBoardManager
import sk.styk.martin.apkanalyzer.core.appanalysis.model.AppDetailData
import sk.styk.martin.apkanalyzer.ui.appdetail.AppDetailFragmentViewModel
import sk.styk.martin.apkanalyzer.ui.appdetail.page.AppDetailPageFragmentViewModel
import sk.styk.martin.apkanalyzer.ui.appdetail.page.provider.AppReceiverDetailListAdapter

class AppReceiverDetailFragmentViewModel @AssistedInject constructor(
    @Assisted appDetailFragmentViewModel: AppDetailFragmentViewModel,
    private val receiverAdapter: AppReceiverDetailListAdapter,
    clipBoardManager: ClipBoardManager,
) : AppDetailPageFragmentViewModel(appDetailFragmentViewModel, receiverAdapter, clipBoardManager) {

    private var receiverData: MutableList<AppReceiverDetailListAdapter.ExpandedBroadcastReceiverData> = mutableListOf()
        set(value) {
            field = value
            receiverAdapter.items = value
        }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        with(receiverAdapter) {
            receiverUpdate.observe(owner) { updateLocalData(it) }
        }
    }

    override fun onDataReceived(appDetailData: AppDetailData): Boolean {
        receiverData = appDetailData.broadcastReceiverData.map { AppReceiverDetailListAdapter.ExpandedBroadcastReceiverData(it, false) }.toMutableList()
        return appDetailData.broadcastReceiverData.isNotEmpty()
    }

    private fun updateLocalData(editedExpandedReceiverData: AppReceiverDetailListAdapter.ExpandedBroadcastReceiverData) {
        receiverData[receiverData.indexOfFirst { it.receiverData == editedExpandedReceiverData.receiverData }] = editedExpandedReceiverData
    }

    @AssistedFactory
    interface Factory {
        fun create(appDetailFragmentViewModel: AppDetailFragmentViewModel): AppReceiverDetailFragmentViewModel
    }
}
