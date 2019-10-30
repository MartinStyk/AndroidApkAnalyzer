package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.feature

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sk.styk.martin.apkanalyzer.databinding.ListItemFeatureDetailBinding
import sk.styk.martin.apkanalyzer.model.detail.FeatureData
import sk.styk.martin.apkanalyzer.ui.base.GenericListAdapter

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