package sk.styk.martin.apkanalyzer.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import sk.styk.martin.apkanalyzer.adapter.detaillist.GenericDetailListAdapter
import sk.styk.martin.apkanalyzer.databinding.ListItemApplicationBinding
import sk.styk.martin.apkanalyzer.model.list.AppListData

/**
 * App list adapter for recycler view.
 * Used in AppListDialog
 *
 * @author Martin Styk
 * @version 05.01.2017.
 */
class AppListRecyclerAdapter(items: List<AppListData>) : GenericDetailListAdapter<AppListData, AppListRecyclerAdapter.ViewHolder>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ListItemApplicationBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(val binding: ListItemApplicationBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AppListData) {
            binding.data = item
            binding.executePendingBindings()
        }
    }
}