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
import sk.styk.martin.apkanalyzer.ui.appdetail.page.DetailInfoDescriptionAdapter
import sk.styk.martin.apkanalyzer.ui.appdetail.page.activity.ARROW_ANIMATION_DURATION
import sk.styk.martin.apkanalyzer.ui.appdetail.page.activity.ROTATION_FLIPPED
import sk.styk.martin.apkanalyzer.ui.appdetail.page.activity.ROTATION_STANDARD
import sk.styk.martin.apkanalyzer.util.TextInfo
import sk.styk.martin.apkanalyzer.util.live.SingleLiveEvent
import javax.inject.Inject

class AppServiceDetailListAdapter @Inject constructor() : DetailInfoDescriptionAdapter<AppServiceDetailListAdapter.ViewHolder>() {

    data class ExpandedServiceData(val serviceData: ServiceData, val expanded: Boolean)

    private val serviceUpdateEvent = SingleLiveEvent<ExpandedServiceData>()
    val serviceUpdate: LiveData<ExpandedServiceData> = serviceUpdateEvent

    var items = emptyList<ExpandedServiceData>()
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

    inner class ServiceDataViewModel(private val expandedServiceData: ExpandedServiceData) {

        val name = expandedServiceData.serviceData.name.substring(expandedServiceData.serviceData.name.lastIndexOf(".") + 1)
        val packageName = expandedServiceData.serviceData.name.substring(0, expandedServiceData.serviceData.name.lastIndexOf("."))
        val expanded = expandedServiceData.expanded

        val permission = DetailInfoAdapter.DetailInfo(
                name = TextInfo.from(R.string.activity_permission),
                value = if (expandedServiceData.serviceData.permission != null) TextInfo.from(expandedServiceData.serviceData.permission) else TextInfo.from(R.string.none),
                description = TextInfo.from(R.string.service_permission_description)
        )
        val exported = DetailInfoAdapter.DetailInfo(
                name = TextInfo.from(R.string.service_exported),
                value = TextInfo.from(if (expandedServiceData.serviceData.isExported) R.string.yes else R.string.no),
                description = TextInfo.from(R.string.service_exported_description)
        )
        val stopWithTask = DetailInfoAdapter.DetailInfo(
                name = TextInfo.from(R.string.service_stop_with_task),
                value = TextInfo.from(if (expandedServiceData.serviceData.isStopWithTask) R.string.yes else R.string.no),
                description = TextInfo.from(R.string.service_stop_with_task_description)
        )
        val singleUser = DetailInfoAdapter.DetailInfo(
                name = TextInfo.from(R.string.service_single_user),
                value = TextInfo.from(if (expandedServiceData.serviceData.isSingleUser) R.string.yes else R.string.no),
                description = TextInfo.from(R.string.service_single_user_description)
        )
        val isolatedProcess = DetailInfoAdapter.DetailInfo(
                name = TextInfo.from(R.string.service_isolated_process),
                value = TextInfo.from(if (expandedServiceData.serviceData.isIsolatedProcess) R.string.yes else R.string.no),
                description = TextInfo.from(R.string.service_isolated_process_description)
        )
        val external = DetailInfoAdapter.DetailInfo(
                name = TextInfo.from(R.string.service_external_service),
                value = TextInfo.from(if (expandedServiceData.serviceData.isExternalService) R.string.yes else R.string.no),
                description = TextInfo.from(R.string.service_external_service_description)
        )

        fun onDetailClick(detailInfo: DetailInfoAdapter.DetailInfo) {
            openDescriptionEvent.value = Description.from(detailInfo)
        }

        fun onLongClick(detailInfo: DetailInfoAdapter.DetailInfo): Boolean {
            copyToClipboardEvent.value = CopyToClipboard.from(detailInfo)
            return true
        }

        fun onTitleLongClick() : Boolean {
            copyToClipboardEvent.value = CopyToClipboard(TextInfo.from(expandedServiceData.serviceData.name))
            return true
        }

        fun toggleExpanded(newlyExpanded: Boolean) {
            serviceUpdateEvent.value = expandedServiceData.copy(expanded = newlyExpanded)
        }

    }

    inner class ViewHolder(val binding: ListItemServiceDetailBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ServiceDataViewModel) {
            binding.viewModel = viewModel
            updateExpandedState(viewModel.expanded)
            binding.headerContainer.setOnClickListener {
                val newlyExpanded = !binding.expandableContainer.isExpanded
                viewModel.toggleExpanded(newlyExpanded)
                binding.expandableContainer.isExpanded = newlyExpanded
                binding.toggleArrow.animate().setDuration(ARROW_ANIMATION_DURATION).rotation(if (newlyExpanded) ROTATION_FLIPPED else ROTATION_STANDARD)
            }
        }

        private fun updateExpandedState(expanded: Boolean) {
            binding.expandableContainer.setExpanded(expanded, false)
            binding.toggleArrow.rotation = if (expanded) ROTATION_FLIPPED else ROTATION_STANDARD
        }
    }

    private inner class ServiceDiffCallback(private val newList: List<ExpandedServiceData>,
                                            private val oldList: List<ExpandedServiceData>) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].serviceData == newList[newItemPosition].serviceData
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition] == newList[newItemPosition]
    }

}