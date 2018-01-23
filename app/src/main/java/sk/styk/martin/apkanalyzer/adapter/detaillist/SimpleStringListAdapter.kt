package sk.styk.martin.apkanalyzer.adapter.detaillist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import sk.styk.martin.apkanalyzer.databinding.ListItemSimpleStringBinding

/**
 * @author Martin Styk
 * @version 07.07.2017.
 */
class SimpleStringListAdapter(items: List<String>) : GenericDetailListAdapter<String, SimpleStringListAdapter.ViewHolder>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ListItemSimpleStringBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(val binding: ListItemSimpleStringBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            binding.value = item
            binding.executePendingBindings()
        }
    }
}