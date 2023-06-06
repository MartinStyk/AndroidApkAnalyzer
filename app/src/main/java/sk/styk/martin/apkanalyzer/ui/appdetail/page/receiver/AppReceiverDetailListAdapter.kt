package sk.styk.martin.apkanalyzer.ui.appdetail.page.provider

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.ListItemReceiverDetailBinding
import sk.styk.martin.apkanalyzer.databinding.ListItemReceiverDetailExpandedBinding
import sk.styk.martin.apkanalyzer.core.appanalysis.model.BroadcastReceiverData
import sk.styk.martin.apkanalyzer.ui.appdetail.adapters.DetailInfoAdapter
import sk.styk.martin.apkanalyzer.ui.appdetail.page.DetailInfoDescriptionAdapter
import sk.styk.martin.apkanalyzer.ui.appdetail.recycler.ExpandableItemViewModel
import sk.styk.martin.apkanalyzer.ui.appdetail.recycler.LazyExpandableViewHolder
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

    inner class ReceiverDataViewModel(private val expandedReceiverData: ExpandedBroadcastReceiverData) : ExpandableItemViewModel {

        val name = expandedReceiverData.receiverData.name.substring(expandedReceiverData.receiverData.name.lastIndexOf(".") + 1)
        val packageName = expandedReceiverData.receiverData.name.substring(0, expandedReceiverData.receiverData.name.lastIndexOf("."))
        override val expanded = expandedReceiverData.expanded

        val permission = DetailInfoAdapter.DetailInfo(
            name = TextInfo.from(R.string.receiver_permission),
            value = expandedReceiverData.receiverData.permission.orNone(),
            description = TextInfo.from(R.string.receiver_permission_description),
        )
        val exported = DetailInfoAdapter.DetailInfo(
            name = TextInfo.from(R.string.receiver_exported),
            value = TextInfo.from(if (expandedReceiverData.receiverData.isExported) R.string.yes else R.string.no),
            description = TextInfo.from(R.string.receiver_exported_description),
        )

        fun onDetailClick(detailInfo: DetailInfoAdapter.DetailInfo) {
            openDescriptionEvent.value = Description.from(detailInfo)
        }

        fun onLongClick(detailInfo: DetailInfoAdapter.DetailInfo): Boolean {
            copyToClipboardEvent.value = CopyToClipboard.from(detailInfo)
            return true
        }

        fun onTitleLongClick(): Boolean {
            copyToClipboardEvent.value = CopyToClipboard(TextInfo.from(expandedReceiverData.receiverData.name))
            return true
        }

        override fun toggleExpanded(newlyExpanded: Boolean) {
            receiverUpdateEvent.value = expandedReceiverData.copy(expanded = newlyExpanded)
        }
    }

    inner class ViewHolder(binding: ListItemReceiverDetailBinding) :
        LazyExpandableViewHolder<ListItemReceiverDetailBinding, ListItemReceiverDetailExpandedBinding, ReceiverDataViewModel>(binding) {

        override fun baseContainer() = baseBinding.container

        override fun expandedInflation() = ListItemReceiverDetailExpandedBinding.inflate(LayoutInflater.from(baseBinding.root.context))

        override fun expandableContainer() = expandedBinding.expandableContainer

        override fun toggleArrow() = baseBinding.toggleArrow

        override fun headerContainer() = baseBinding.headerContainer
    }

    private inner class ReceiverDiffCallback(
        private val newList: List<ExpandedBroadcastReceiverData>,
        private val oldList: List<ExpandedBroadcastReceiverData>,
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].receiverData == newList[newItemPosition].receiverData
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition] == newList[newItemPosition]
    }
}
