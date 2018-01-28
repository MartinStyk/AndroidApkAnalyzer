package sk.styk.martin.apkanalyzer.ui.adapter.detaillist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import sk.styk.martin.apkanalyzer.databinding.ListItemReceiverDetailBinding
import sk.styk.martin.apkanalyzer.model.detail.BroadcastReceiverData

/**
 * @author Martin Styk
 * @version 07.07.2017.
 */
class ReceiverListAdapter(items: List<BroadcastReceiverData>) : GenericDetailListAdapter<BroadcastReceiverData, ReceiverListAdapter.ViewHolder>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ListItemReceiverDetailBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(val binding: ListItemReceiverDetailBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BroadcastReceiverData) {
            binding.data = item
            binding.executePendingBindings()
        }
    }
}