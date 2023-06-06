package sk.styk.martin.apkanalyzer.ui.appdetail.page.activity

import androidx.lifecycle.LifecycleOwner
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import sk.styk.martin.apkanalyzer.manager.clipboard.ClipBoardManager
import sk.styk.martin.apkanalyzer.core.appanalysis.model.AppDetailData
import sk.styk.martin.apkanalyzer.ui.appdetail.AppDetailFragmentViewModel
import sk.styk.martin.apkanalyzer.ui.appdetail.page.AppDetailPageFragmentViewModel

class AppActivityDetailFragmentViewModel @AssistedInject constructor(
    @Assisted appDetailFragmentViewModel: AppDetailFragmentViewModel,
    val activityAdapter: AppActivityDetailListAdapter,
    clipBoardManager: ClipBoardManager,
) : AppDetailPageFragmentViewModel(appDetailFragmentViewModel, activityAdapter, clipBoardManager) {

    val runActivity = activityAdapter.runActivity

    private var activityData: MutableList<AppActivityDetailListAdapter.ExpandedActivityData> = mutableListOf()
        set(value) {
            field = value
            activityAdapter.items = value
        }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        with(activityAdapter) {
            activityUpdate.observe(owner) { updateLocalData(it) }
        }
    }

    override fun onDataReceived(appDetailData: AppDetailData): Boolean {
        activityData = appDetailData.activityData.map { AppActivityDetailListAdapter.ExpandedActivityData(it, false) }.toMutableList()
        return appDetailData.activityData.isNotEmpty()
    }

    private fun updateLocalData(editedExpandedActivityData: AppActivityDetailListAdapter.ExpandedActivityData) {
        activityData[activityData.indexOfFirst { it.activityData == editedExpandedActivityData.activityData }] = editedExpandedActivityData
    }

    @AssistedFactory
    interface Factory {
        fun create(appDetailFragmentViewModel: AppDetailFragmentViewModel): AppActivityDetailFragmentViewModel
    }
}
