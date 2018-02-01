package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.feature

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import sk.styk.martin.apkanalyzer.databinding.ListItemFeatureDetailBinding
import sk.styk.martin.apkanalyzer.model.detail.FeatureData
import sk.styk.martin.apkanalyzer.ui.base.GenericListAdapter

/**
 * @author Martin Styk
 * @version 07.07.2017.
 */
class FeatureDetailListAdapter(override val presenter: FeatureDetailPageContract.Presenter) : GenericListAdapter<FeatureDetailListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ListItemFeatureDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    inner class ViewHolder(val binding: ListItemFeatureDetailBinding) : FeatureDetailPageContract.ItemView, RecyclerView.ViewHolder(binding.root) {
        override fun bind(item: FeatureData) {
            binding.data = item
            binding.executePendingBindings()
        }
    }
}