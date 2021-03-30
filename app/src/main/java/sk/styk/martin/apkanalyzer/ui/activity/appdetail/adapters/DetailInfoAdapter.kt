package sk.styk.martin.apkanalyzer.ui.activity.appdetail.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import sk.styk.martin.apkanalyzer.databinding.ListItemDetailBinding
import sk.styk.martin.apkanalyzer.util.TextInfo
import sk.styk.martin.apkanalyzer.util.live.SingleLiveEvent
import javax.inject.Inject

class DetailInfoAdapter @Inject constructor() : RecyclerView.Adapter<DetailInfoAdapter.ViewHolder>() {

    data class DetailInfo(
            val name: TextInfo,
            val value: TextInfo,
            val description: TextInfo
    )

    private val openDescriptionEvent = SingleLiveEvent<DetailInfo>()
    val openDescription: LiveData<DetailInfo> = openDescriptionEvent

    private val copyToClipboardEvent = SingleLiveEvent<DetailInfo>()
    val copyToClipboard: LiveData<DetailInfo> = copyToClipboardEvent

    var info = emptyList<DetailInfo>()
        set(value) {
            val diffResult = DiffUtil.calculateDiff(InfoDiffCallback(value, field))
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    override fun getItemCount(): Int = info.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.update(DetailItemViewModel(info[position]))
    }

    inner class DetailItemViewModel(val info: DetailInfo) {
        fun onClick() {
            openDescriptionEvent.value = info
        }

        fun onLongClick(): Boolean {
            copyToClipboardEvent.value = info
            return true
        }
    }

    inner class ViewHolder(private val binding: ListItemDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        fun update(viewModel: DetailItemViewModel) {
            binding.viewModel = viewModel
        }
    }

    private inner class InfoDiffCallback(private val newList: List<DetailInfo>,
                                         private val oldList: List<DetailInfo>) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].name == newList[newItemPosition].name
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition] == newList[newItemPosition]
    }
}