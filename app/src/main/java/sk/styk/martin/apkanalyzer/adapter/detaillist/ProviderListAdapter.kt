package sk.styk.martin.apkanalyzer.adapter.detaillist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.databinding.ListItemProviderDetailBinding
import sk.styk.martin.apkanalyzer.model.detail.ContentProviderData
import sk.styk.martin.apkanalyzer.view.DetailListItemView

/**
 * @author Martin Styk
 * @version 07.07.2017.
 */
class ProviderListAdapter(items: List<ContentProviderData>) : GenericDetailListAdapter<ContentProviderData, ProviderListAdapter.ViewHolder>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context);
        val itemBinding = ListItemProviderDetailBinding.inflate(layoutInflater, parent, false);
        return ViewHolder(itemBinding);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(val binding: ListItemProviderDetailBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ContentProviderData) {
            binding.data = item
            binding.executePendingBindings()
        }
    }
}