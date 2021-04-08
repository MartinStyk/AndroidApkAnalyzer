package sk.styk.martin.apkanalyzer.ui.appdetail.page.provider

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.ListItemReceiverDetailBinding
import sk.styk.martin.apkanalyzer.model.detail.BroadcastReceiverData
import sk.styk.martin.apkanalyzer.ui.appdetail.adapters.DetailInfoAdapter
import sk.styk.martin.apkanalyzer.ui.appdetail.page.DetailInfoDescriptionAdapter
import sk.styk.martin.apkanalyzer.ui.appdetail.page.activity.ARROW_ANIMATION_DURATION
import sk.styk.martin.apkanalyzer.ui.appdetail.page.activity.ROTATION_FLIPPED
import sk.styk.martin.apkanalyzer.ui.appdetail.page.activity.ROTATION_STANDARD
import sk.styk.martin.apkanalyzer.util.TextInfo
import sk.styk.martin.apkanalyzer.util.live.SingleLiveEvent
import javax.inject.Inject

class AppReceiverDetailListAdapter @Inject constructor() : DetailInfoDescriptionAdapter<AppReceiverDetailListAdapter.ViewHolder>() {

    data class ExpandedBroadcastReceiverData(val receiverData: BroadcastReceiverData, val expanded: Boolean)

    private val receiverUpdateEvent = SingleLiveEvent<ExpandedBroadcastReceiverData>()
    val receiverUpdate: LiveData<ExpandedBroadcastReceiverData> = receiverUpdateEvent

    var items = emptyList<ExpandedBroadcastReceiverData>()
        set(value) {
            val diffResult = DiffUtil.calculateDiff(ReceiverDiffCallback(value, field))
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ListItemReceiverDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ReceiverDataViewModel(items[position]))
    }

    override fun getItemCount() = items.size

    inner class ReceiverDataViewModel(private val expandedReceiverData: ExpandedBroadcastReceiverData) {

        val name = expandedReceiverData.receiverData.name.substring(expandedReceiverData.receiverData.name.lastIndexOf(".") + 1)
        val packageName = expandedReceiverData.receiverData.name.substring(0, expandedReceiverData.receiverData.name.lastIndexOf("."))
        val expanded = expandedReceiverData.expanded

        val permission = DetailInfoAdapter.DetailInfo(
                name = TextInfo.from(R.string.receiver_permission),
                value = if (expandedReceiverData.receiverData.permission != null) TextInfo.from(expandedReceiverData.receiverData.permission) else TextInfo.from(R.string.none),
                description = TextInfo.from(R.string.receiver_permission_description)
        )
        val exported = DetailInfoAdapter.DetailInfo(
                name = TextInfo.from(R.string.receiver_exported),
                value = TextInfo.from(if (expandedReceiverData.receiverData.isExported) R.string.yes else R.string.no),
                description = TextInfo.from(R.string.receiver_exported_description)
        )

        fun onDetailClick(detailInfo: DetailInfoAdapter.DetailInfo) {
            openDescriptionEvent.value = Description.from(detailInfo)
        }

        fun onLongClick(detailInfo: DetailInfoAdapter.DetailInfo): Boolean {
            copyToClipboardEvent.value = CopyToClipboard.from(detailInfo)
            return true
        }

        fun onTitleLongClick() : Boolean {
            copyToClipboardEvent.value = CopyToClipboard(TextInfo.from(expandedReceiverData.receiverData.name))
            return true
        }

        fun toggleExpanded(newlyExpanded: Boolean) {
            receiverUpdateEvent.value = expandedReceiverData.copy(expanded = newlyExpanded)
        }

    }

    inner class ViewHolder(val binding: ListItemReceiverDetailBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ReceiverDataViewModel) {
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

    private inner class ReceiverDiffCallback(private val newList: List<ExpandedBroadcastReceiverData>,
                                             private val oldList: List<ExpandedBroadcastReceiverData>) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].receiverData == newList[newItemPosition].receiverData
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition] == newList[newItemPosition]
    }

}