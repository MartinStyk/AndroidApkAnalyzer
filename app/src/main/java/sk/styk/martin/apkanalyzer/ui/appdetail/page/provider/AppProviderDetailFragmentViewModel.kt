package sk.styk.martin.apkanalyzer.ui.appdetail.page.provider

import androidx.lifecycle.LifecycleOwner
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import sk.styk.martin.apkanalyzer.manager.clipboard.ClipBoardManager
import sk.styk.martin.apkanalyzer.core.appanalysis.model.AppDetailData
import sk.styk.martin.apkanalyzer.ui.appdetail.AppDetailFragmentViewModel
import sk.styk.martin.apkanalyzer.ui.appdetail.page.AppDetailPageFragmentViewModel

class AppProviderDetailFragmentViewModel @AssistedInject constructor(
    @Assisted appDetailFragmentViewModel: AppDetailFragmentViewModel,
    private val providerAdapter: AppProviderDetailListAdapter,
    clipBoardManager: ClipBoardManager,
) : AppDetailPageFragmentViewModel(appDetailFragmentViewModel, providerAdapter, clipBoardManager) {

    private var providerData: MutableList<AppProviderDetailListAdapter.ExpandedContentProviderData> = mutableListOf()
        set(value) {
            field = value
            providerAdapter.items = value
        }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        with(providerAdapter) {
            providerUpdate.observe(owner) { updateLocalData(it) }
        }
    }

    override fun onDataReceived(appDetailData: AppDetailData): Boolean {
        providerData = appDetailData.contentProviderData.map { AppProviderDetailListAdapter.ExpandedContentProviderData(it, false) }.toMutableList()
        return appDetailData.contentProviderData.isNotEmpty()
    }

    private fun updateLocalData(editedExpandedProviderData: AppProviderDetailListAdapter.ExpandedContentProviderData) {
        providerData[providerData.indexOfFirst { it.contentProviderData == editedExpandedProviderData.contentProviderData }] = editedExpandedProviderData
    }

    @AssistedFactory
    interface Factory {
        fun create(appDetailFragmentViewModel: AppDetailFragmentViewModel): AppProviderDetailFragmentViewModel
    }
}
