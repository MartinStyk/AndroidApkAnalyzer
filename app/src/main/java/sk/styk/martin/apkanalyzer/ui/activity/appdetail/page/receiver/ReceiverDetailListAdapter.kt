package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.receiver

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sk.styk.martin.apkanalyzer.databinding.ListItemReceiverDetailBinding
import sk.styk.martin.apkanalyzer.model.detail.BroadcastReceiverData
import sk.styk.martin.apkanalyzer.ui.base.GenericListAdapter

class ReceiverDetailListAdapter(override val presenter: ReceiverDetailPageContract.Presenter) : GenericListAdapter<ReceiverDetailListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ListItemReceiverDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    inner class ViewHolder(val binding: ListItemReceiverDetailBinding) : ReceiverDetailPageContract.ItemView, RecyclerView.ViewHolder(binding.root) {
        override fun bind(item: BroadcastReceiverData) {
            binding.data = item
            binding.executePendingBindings()
        }
    }
}