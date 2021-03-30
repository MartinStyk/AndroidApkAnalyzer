package sk.styk.martin.apkanalyzer.ui.appdetail.page.service

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.ListItemServiceDetailBinding
import sk.styk.martin.apkanalyzer.model.detail.ServiceData
import sk.styk.martin.apkanalyzer.ui.appdetail.adapters.DetailInfoAdapter
import sk.styk.martin.apkanalyzer.util.TextInfo
import sk.styk.martin.apkanalyzer.util.live.SingleLiveEvent
import javax.inject.Inject

class AppServiceDetailListAdapter @Inject constructor() : RecyclerView.Adapter<AppServiceDetailListAdapter.ViewHolder>() {

    private val openDescriptionEvent = SingleLiveEvent<DetailInfoAdapter.DetailInfo>()
    val openDescription: LiveData<DetailInfoAdapter.DetailInfo> = openDescriptionEvent

    private val copyToClipboardEvent = SingleLiveEvent<DetailInfoAdapter.DetailInfo>()
    val copyToClipboard: LiveData<DetailInfoAdapter.DetailInfo> = copyToClipboardEvent

    var items = emptyList<ServiceData>()
        set(value) {
            val diffResult = DiffUtil.calculateDiff(ServiceDiffCallback(value, field))
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ListItemServiceDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ServiceDataViewModel(items[position]))
    }

    override fun getItemCount() = items.size

    inner class ServiceDataViewModel(private val serviceData: ServiceData) {

        val name = serviceData.name
        val permission = DetailInfoAdapter.DetailInfo(
                name = TextInfo.from(R.string.activity_permission),
                value = if (serviceData.permission != null) TextInfo.from(serviceData.permission) else TextInfo.from(R.string.none),
                description = TextInfo.from(R.string.service_permission_description)
        )
        val exported = DetailInfoAdapter.DetailInfo(
                name = TextInfo.from(R.string.service_exported),
                value = TextInfo.from(if (serviceData.isExported) R.string.yes else R.string.no),
                description = TextInfo.from(R.string.service_exported_description)
        )
        val stopWithTask = DetailInfoAdapter.DetailInfo(
                name = TextInfo.from(R.string.service_stop_with_task),
                value = TextInfo.from(if (serviceData.isStopWithTask) R.string.yes else R.string.no),
                description = TextInfo.from(R.string.service_stop_with_task_description)
        )
        val singleUser = DetailInfoAdapter.DetailInfo(
                name = TextInfo.from(R.string.service_single_user),
                value = TextInfo.from(if (serviceData.isSingleUser) R.string.yes else R.string.no),
                description = TextInfo.from(R.string.service_single_user_description)
        )
        val isolatedProcess = DetailInfoAdapter.DetailInfo(
                name = TextInfo.from(R.string.service_isolated_process),
                value = TextInfo.from(if (serviceData.isIsolatedProcess) R.string.yes else R.string.no),
                description = TextInfo.from(R.string.service_isolated_process_description)
        )
        val external = DetailInfoAdapter.DetailInfo(
                name = TextInfo.from(R.string.service_external_service),
                value = TextInfo.from(if (serviceData.isExternalService) R.string.yes else R.string.no),
                description = TextInfo.from(R.string.service_external_service_description)
        )

        fun onDetailClick(detailInfo: DetailInfoAdapter.DetailInfo) {
            openDescriptionEvent.value = detailInfo
        }

        fun onLongClick(detailInfo: DetailInfoAdapter.DetailInfo): Boolean {
            copyToClipboardEvent.value = detailInfo
            return true
        }

    }

    inner class ViewHolder(val binding: ListItemServiceDetailBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ServiceDataViewModel) {
            binding.viewModel = viewModel
        }
    }

    private inner class ServiceDiffCallback(private val newList: List<ServiceData>,
                                            private val oldList: List<ServiceData>) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].name == newList[newItemPosition].name
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition] == newList[newItemPosition]
    }

}