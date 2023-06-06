package sk.styk.martin.apkanalyzer.ui.appdetail.page.service

import androidx.lifecycle.LifecycleOwner
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import sk.styk.martin.apkanalyzer.manager.clipboard.ClipBoardManager
import sk.styk.martin.apkanalyzer.core.appanalysis.model.AppDetailData
import sk.styk.martin.apkanalyzer.ui.appdetail.AppDetailFragmentViewModel
import sk.styk.martin.apkanalyzer.ui.appdetail.page.AppDetailPageFragmentViewModel

class AppServiceDetailFragmentViewModel @AssistedInject constructor(
    @Assisted appDetailFragmentViewModel: AppDetailFragmentViewModel,
    private val serviceAdapter: AppServiceDetailListAdapter,
    clipBoardManager: ClipBoardManager,
) : AppDetailPageFragmentViewModel(appDetailFragmentViewModel, serviceAdapter, clipBoardManager) {

    private var serviceData: MutableList<AppServiceDetailListAdapter.ExpandedServiceData> = mutableListOf()
        set(value) {
            field = value
            serviceAdapter.items = serviceData
        }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        with(serviceAdapter) {
            serviceUpdate.observe(owner) { updateLocalData(it) }
        }
    }

    override fun onDataReceived(appDetailData: AppDetailData): Boolean {
        serviceData = appDetailData.serviceData.map { AppServiceDetailListAdapter.ExpandedServiceData(it, false) }.toMutableList()
        return appDetailData.serviceData.isNotEmpty()
    }

    private fun updateLocalData(editedExpandedServiceData: AppServiceDetailListAdapter.ExpandedServiceData) {
        serviceData[serviceData.indexOfFirst { it.serviceData == editedExpandedServiceData.serviceData }] = editedExpandedServiceData
    }

    @AssistedFactory
    interface Factory {
        fun create(appDetailFragmentViewModel: AppDetailFragmentViewModel): AppServiceDetailFragmentViewModel
    }
}
