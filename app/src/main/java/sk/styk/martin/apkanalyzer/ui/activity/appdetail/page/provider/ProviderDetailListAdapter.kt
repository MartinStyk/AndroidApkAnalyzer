package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.provider

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import sk.styk.martin.apkanalyzer.databinding.ListItemProviderDetailBinding
import sk.styk.martin.apkanalyzer.model.detail.ContentProviderData
import sk.styk.martin.apkanalyzer.ui.base.GenericListAdapter

/**
 * @author Martin Styk
 * @version 07.07.2017.
 */
class ProviderDetailListAdapter(override val presenter: ProviderDetailPageContract.Presenter) : GenericListAdapter<ProviderDetailListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ListItemProviderDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    inner class ViewHolder(val binding: ListItemProviderDetailBinding) : ProviderDetailPageContract.ItemView, RecyclerView.ViewHolder(binding.root) {
        override fun bind(item: ContentProviderData) {
            binding.data = item
            binding.executePendingBindings()
        }
    }
}