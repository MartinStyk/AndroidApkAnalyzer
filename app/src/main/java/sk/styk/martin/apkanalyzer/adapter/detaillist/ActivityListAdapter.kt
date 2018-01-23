package sk.styk.martin.apkanalyzer.adapter.detaillist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import sk.styk.martin.apkanalyzer.databinding.ListItemActivityDetailBinding
import sk.styk.martin.apkanalyzer.model.detail.ActivityData

/**
 * @author Martin Styk
 * @version 07.07.2017.
 */
class ActivityListAdapter(items: List<ActivityData>) : GenericDetailListAdapter<ActivityData, ActivityListAdapter.ViewHolder>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ListItemActivityDetailBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(val binding: ListItemActivityDetailBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ActivityData) {
            binding.data = item
            binding.executePendingBindings()
        }
    }

}