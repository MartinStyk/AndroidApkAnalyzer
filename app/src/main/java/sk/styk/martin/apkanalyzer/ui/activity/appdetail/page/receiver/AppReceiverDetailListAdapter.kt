package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.provider

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.ListItemReceiverDetailBinding
import sk.styk.martin.apkanalyzer.model.detail.BroadcastReceiverData
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.adapters.DetailInfoAdapter
import sk.styk.martin.apkanalyzer.util.TextInfo
import sk.styk.martin.apkanalyzer.util.live.SingleLiveEvent
import javax.inject.Inject

class AppReceiverDetailListAdapter @Inject constructor() : RecyclerView.Adapter<AppReceiverDetailListAdapter.ViewHolder>() {

    private val openDescriptionEvent = SingleLiveEvent<DetailInfoAdapter.DetailInfo>()
    val openDescription: LiveData<DetailInfoAdapter.DetailInfo> = openDescriptionEvent

    private val copyToClipboardEvent = SingleLiveEvent<DetailInfoAdapter.DetailInfo>()
    val copyToClipboard: LiveData<DetailInfoAdapter.DetailInfo> = copyToClipboardEvent

    var items = emptyList<BroadcastReceiverData>()
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

    inner class ReceiverDataViewModel(private val receiverData: BroadcastReceiverData) {

        val name = receiverData.name

        val permission = DetailInfoAdapter.DetailInfo(
                name = TextInfo.from(R.string.receiver_permission),
                value = if (receiverData.permission != null) TextInfo.from(receiverData.permission) else TextInfo.from(R.string.none),
                description = TextInfo.from(R.string.receiver_permission_description)
        )
        val exported = DetailInfoAdapter.DetailInfo(
                name = TextInfo.from(R.string.receiver_exported),
                value = TextInfo.from(if (receiverData.isExported) R.string.yes else R.string.no),
                description = TextInfo.from(R.string.receiver_exported_description)
        )

        fun onDetailClick(detailInfo: DetailInfoAdapter.DetailInfo) {
            openDescriptionEvent.value = detailInfo
        }

        fun onLongClick(detailInfo: DetailInfoAdapter.DetailInfo): Boolean {
            copyToClipboardEvent.value = detailInfo
            return true
        }
    }

    inner class ViewHolder(val binding: ListItemReceiverDetailBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ReceiverDataViewModel) {
            binding.viewModel = viewModel
        }
    }

    private inner class ReceiverDiffCallback(private val newList: List<BroadcastReceiverData>,
                                             private val oldList: List<BroadcastReceiverData>) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].name == newList[newItemPosition].name
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition] == newList[newItemPosition]
    }

}