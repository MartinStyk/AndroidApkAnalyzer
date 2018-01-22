package sk.styk.martin.apkanalyzer.adapter.detaillist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import sk.styk.martin.apkanalyzer.databinding.ListItemFeatureDetailBinding
import sk.styk.martin.apkanalyzer.model.detail.FeatureData

/**
 * @author Martin Styk
 * @version 12.10.2017.
 */
class FeatureListAdapter(items: List<FeatureData>) : GenericDetailListAdapter<FeatureData, FeatureListAdapter.ViewHolder>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context);
        val itemBinding = ListItemFeatureDetailBinding.inflate(layoutInflater, parent, false);
        return ViewHolder(itemBinding);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(val binding: ListItemFeatureDetailBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FeatureData) {
            binding.data = item
            binding.executePendingBindings()
        }
    }
}